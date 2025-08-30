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

    if (!res.ok) throw new Error(`Failed to fetch ${bucket} applications`);

    renderApplications(await res.json(), bucket.toLowerCase());
  } catch (err) {
    console.error(err);
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
    await fetch("/approver/applications/claim", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ applicantId }),
    });
    loadUnclaimedApplications();
    //loadClaimedApplications();
  } catch (err) {
    console.error(err);
  }
}

async function reviewWorkflow(applicantId) {
  try {
    const res = await fetch("/approver/applications/workflows", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ applicationId: applicantId }),
    });

    if (!res.ok) throw new Error("Failed to fetch workflow");
    const steps = await res.json();
    showWorkflowModal(steps, applicantId);
  } catch (err) {
    console.error(err);
  }
}


function showWorkflowModal(steps, applicantId) {
  const container = document.getElementById("workflow-steps-container");
  container.innerHTML = "";

  const currentStep = steps.find((step) => step.current);
  console.log(currentStep);


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
    const workflow = await res.json();

    if (workflow.applicant.status === "Approved") {
      closeWorkflowModal();
    } else {
      reviewWorkflow(applicantId); 
    }

    loadClaimedApplications();
   // loadApprovedApplications();
  } catch (err) {
    console.error(err);
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
    await res.json();
    closeWorkflowModal();
    loadClaimedApplications();
    //loadRejectedApplications();
  } catch (err) {
    console.error(err);
  }
}

function closeWorkflowModal() {
  document.getElementById("workflow-modal").style.display = "none";
  
}

document.addEventListener("DOMContentLoaded", () => {
  showTab("unclaimed"); q
});
