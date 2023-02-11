// example.js

async function makeRequest() {
    const response = await fetch('/deposit', {
      method: 'POST',
      body: JSON.stringify({ amount: 100 }),
      headers: {
        'Content-Type': 'application/json'
      }
    });
  
    const data = await response.json();
  
    console.log(data);
  }
  
  document.getElementById('deposit-button').addEventListener('click', makeRequest);
  