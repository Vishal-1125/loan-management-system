function showTab(tab) {
  document.querySelectorAll(".tab").forEach((t) => t.classList.remove("active"));
  document.getElementById(tab + "-tab").classList.add("active");

  switch (tab) {
    case "unclaimed":
      loadUnclaimedApplications();
      break;
    case "claimed":
      loadClaimedApplications();
      break;
    case "approved":
      loadApprovedApplications();
      break;
    case "rejected":
      loadRejectedApplications();
      break;
  }
}

async function loadApplications(bucket) {
  try {
    const res = await fetch("/approver/applications/", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ applicationBucket: bucket }),
    });

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || `Failed to fetch ${bucket} applications`);
      console.error(responseData);
      return;
    }

    renderApplications(responseData.data, bucket.toLowerCase());
  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}

function loadUnclaimedApplications() { return loadApplications("Unclaimed"); }
function loadClaimedApplications() { return loadApplications("Claimed"); }
function loadApprovedApplications() { return loadApplications("Approved"); }
function loadRejectedApplications() { return loadApplications("Rejected"); }

function renderApplications(apps, tab) {
  const tbody = document.getElementById("applications-table-body");
  tbody.innerHTML = "";
  apps.forEach((app) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${app.applicantId}</td>
      <td>${app.name}</td>
      <td>${app.status}</td>
      <td>
        ${
          tab === "unclaimed"
            ? `<button onclick="claimApplication(${app.applicantId})">Claim</button>`
            : ""
        }
        ${
          tab === "claimed"
            ? `<button onclick="reviewWorkflow(${app.applicantId})">Review</button>`
            : ""
        }
      </td>
    `;
    tbody.appendChild(row);
  });
}

async function claimApplication(applicantId) {
  try {
    const res = await fetch("/approver/applications/claim", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ applicantId }),
    });

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || "Error claiming application");
      console.error(responseData);
      return;
    }

    loadUnclaimedApplications();
  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}

async function reviewWorkflow(applicantId) {
  try {
    const res = await fetch("/approver/applications/workflows", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ applicationId: applicantId }),
    });

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || "Failed to fetch workflow");
      console.error(responseData);
      return;
    }

    const steps = responseData.data;
    showWorkflowModal(steps, applicantId);
  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}

function showWorkflowModal(steps, applicantId) {
  const container = document.getElementById("workflow-steps-container");
  container.innerHTML = "";

  const currentStep = steps.find((step) => step.current);
  console.log(currentStep);

  if (!currentStep) {
    container.innerHTML = "<strong>All steps completed!</strong>";
    return;
  }

  const div = document.createElement("div");
  div.innerHTML = `
    <strong>${currentStep.stepName}</strong> - ${currentStep.status}<br>
    Comments: ${currentStep.comments || ""}<br>
    <input type="text" id="comment-${currentStep.stepName}" placeholder="Add comment">
    <button onclick="approveStep(${applicantId}, '${currentStep.stepName}')">Approve</button>
    <button onclick="rejectStep(${applicantId}, '${currentStep.stepName}')">Reject</button>
    <hr>
  `;
  container.appendChild(div);

  document.getElementById("workflow-modal").style.display = "block";
}

async function approveStep(applicantId, stepName) {
  try {
    const res = await fetch("/approver/applications/approve", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        applicantId,
        comments: document.getElementById(`comment-${stepName}`).value,
      }),
    });

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || "Error approving step");
      console.error(responseData);
      return;
    }

    const workflow = responseData.data;

    if (workflow.applicant.status === "Approved") {
      closeWorkflowModal();
    } else {
      reviewWorkflow(applicantId); 
    }

    loadClaimedApplications();
  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}

async function rejectStep(applicantId, stepName) {
  try {
    const res = await fetch("/approver/applications/reject", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        applicantId,
        comments: document.getElementById(`comment-${stepName}`).value,
      }),
    });

    const responseData = await res.json();

    if (responseData.status !== "0") {
      alert(responseData.message || "Error rejecting step");
      console.error(responseData);
      return;
    }

    closeWorkflowModal();
    loadClaimedApplications();
  } catch (err) {
    console.error(err);
    alert("Network error. Please try again.");
  }
}

function closeWorkflowModal() {
  document.getElementById("workflow-modal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {
  showTab("unclaimed");
});
