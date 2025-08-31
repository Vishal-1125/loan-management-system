function showTab(tab) {
  document.getElementById("punch").style.display =
    tab === "punch" ? "block" : "none";
  document.getElementById("myapps").style.display =
    tab === "myapps" ? "block" : "none";
  document
    .querySelectorAll(".tab")
    .forEach((t) => t.classList.remove("active"));
  event.target.classList.add("active");
  if (tab === "myapps") loadMyApplications();
}

document
  .getElementById("punch-form")
  .addEventListener("submit", async function (e) {
    e.preventDefault();

    const payload = {
      name: document.getElementById("name").value,
      loanAmount: document.getElementById("loanAmount").value,
      tenureMonths: document.getElementById("tenure").value,
      income: document.getElementById("income").value,
      contactDetails: document.getElementById("contact").value,
    };

  try {
    const res = await fetch("/ro/punchApplication", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    const responseData = await res.json(); 

    if (responseData.status === "0") {
        alert(responseData.message || "Application submitted!");
        document.getElementById("punch-form").reset();
    } else {
        alert(responseData.message || "Error submitting application. Please try again.");
        console.error(responseData);
    }
} catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
}

  });

async function loadMyApplications() {
  try {
    const res = await fetch("/ro/viewApplications");

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || "Error loading applications.");
      console.error(responseData);
      return;
    }

    const applications = responseData.data || [];
    const tbody = document.querySelector("#myapps-table-body");
    tbody.innerHTML = "";

    applications.forEach((app) => {
      const tr = `<tr>
                      <td>${app.applicantId}</td>
                      <td>${app.name}</td>
                      <td>${app.loanAmount}</td>
                      <td>${app.tenureMonths}</td>
                      <td>${app.status}</td>
                  </tr>`;
      tbody.innerHTML += tr;
    });

  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}
