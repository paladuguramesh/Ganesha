// This is your test publishable API key.
const stripe = Stripe(publicKey);

// The items the customer wants to buy
const request = {
    amount: amount,
    email: email,
    productName: productName
}

const orderIdValue = orderId;

console.log(publicKey);
console.log(request);
console.log(orderIdValue);

let elements;

initialize();
checkStatus();

document
    .querySelector("#payment-form")
    .addEventListener("submit", handleSubmit);

let emailAddress = '';
// Fetches a payment intent and captures the client secret

let paymentIntentID = '';
async function initialize() {
    const response = await fetch("create-payment-intent", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(request),
    });

    const { intentID, clientSecret } = await response.json();

    paymentIntentID = intentID;

    const appearance = {
        theme: 'stripe',
    };
    elements = stripe.elements({ appearance, clientSecret });

    const linkAuthenticationElement = elements.create("linkAuthentication");
    linkAuthenticationElement.mount("#link-authentication-element");

    linkAuthenticationElement.on('change', (event) => {
        emailAddress = event.value.email;
    });

    const paymentElementOptions = {
        layout: "tabs",
        defaultValues: {
            billingDetails:{
                email: request.email
            }
        }
    };

    const paymentElement = elements.create("payment", paymentElementOptions);
    paymentElement.mount("#payment-element");
}

console.log(paymentIntentID);

async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);

    const { error } = await stripe.confirmPayment({
        elements,
        confirmParams: {
            // Make sure to change this to your payment completion page
            return_url: "http://clayganeshaseattle.com/confirmPayment?paymentId="+paymentIntentID + "&orderId=" + orderIdValue,
            receipt_email: emailAddress
        },
    });

    if (error.type === "card_error" || error.type === "validation_error") {
        showMessage(error.message);
    } else {
        showMessage("An unexpected error occurred.");
    }

    setLoading(false);
}

// Fetches the payment intent status after payment submission
async function checkStatus() {
    const clientSecret = new URLSearchParams(window.location.search).get(
        "payment_intent_client_secret"
    );

    if (!clientSecret) {
        return;
    }

    const { paymentIntent } = await stripe.retrievePaymentIntent(clientSecret);

    switch (paymentIntent.status) {
        case "succeeded":
            showMessage("Payment succeeded!");
            break;
        case "processing":
            showMessage("Your payment is processing.");
            break;
        case "requires_payment_method":
            showMessage("Your payment was not successful, please try again.");
            break;
        default:
            showMessage("Something went wrong.");
            break;
    }
}

// ------- UI helpers -------

function showMessage(messageText) {
    const messageContainer = document.querySelector("#payment-message");

    messageContainer.classList.remove("hidden");
    messageContainer.textContent = messageText;

    setTimeout(function () {
        messageContainer.classList.add("hidden");
        messageContainer.textContent = "";
    }, 4000);
}

// Show a spinner on payment submission
function setLoading(isLoading) {
    if (isLoading) {
        // Disable the button and show a spinner
        document.querySelector("#submit").disabled = true;
        document.querySelector("#spinner").classList.remove("hidden");
        document.querySelector("#button-text").classList.add("hidden");
    } else {
        document.querySelector("#submit").disabled = false;
        document.querySelector("#spinner").classList.add("hidden");
        document.querySelector("#button-text").classList.remove("hidden");
    }
}