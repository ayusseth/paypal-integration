<h1 align="center">🚀 PayPal Checkout Microservice  
<br/> <small>(OAuth + Create Order + Capture Order)</small></h1>

<p align="center">
<img src="https://img.shields.io/badge/Java-17-blue?logo=oracle"/> 
<img src="https://img.shields.io/badge/SpringBoot-Microservice-brightgreen?logo=springboot"/> 
<img src="https://img.shields.io/badge/PayPal-REST%20API-003087?logo=paypal"/>  
</p>

<p align="center">
A production-grade backend service designed using <strong>Spring Boot</strong>, implementing:
<br/>
<strong>OAuth Access Token → Create Order → Capture Order</strong>
<br/>
with complete <strong>Custom HTTP Engine</strong>, <strong>Error Handling</strong>, and <strong>Validation</strong>.
</p>

<hr/>

<h2>✨ Overview</h2>
<p>
This microservice provides end-to-end integration with PayPal’s REST API.  
It handles authentication, order creation, capture logic, error processing, validation, and encapsulates
all request/response models as per PayPal specifications.
</p>

<ul>
  <li>🔐 Generate OAuth Access Token</li>
  <li>🛒 Create Order with request models</li>
  <li>💳 Capture Order to finalize payments</li>
  <li>⚙️ Custom HTTP Client (No RestTemplate/WebClient)</li>
  <li>❌ Centralized Error Handling with enums</li>
  <li>🧪 Request Validation Layer</li>
  <li>🧩 Helper & Utility Layer</li>
</ul>

<hr/>

<h2>📁 Folder Structure</h2>

<pre>
src/main/java/com/ayush/paypal/
│
├── config/
│   └── AppConfig.java
│
├── constant/
│   ├── Constant.java
│   └── ErrorCodeEnum.java
│
├── controller/
│   └── PaymentController.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── PaypalProviderException.java
│
├── http/
│   ├── HttpRequest.java
│   └── HttpServiceEngine.java
│
├── paypal/
│   ├── req/
│   │   ├── Amount.java
│   │   ├── PurchaseUnit.java
│   │   ├── ExperienceContext.java
│   │   ├── PaymentSource.java
│   │   ├── OrderRequest.java
│   │   └── Paypal.java
│   │
│   └── res/
│       ├── PaypalOauthToken.java
│       ├── PaypalOrder.java
│       ├── PaypalLink.java
│       ├── PaypalErrorDetail.java
│       ├── PaypalErrorLink.java
│       └── PaypalErrorResponse.java
│
├── pojo/
│   ├── CreateOrderReq.java
│   ├── OrderResponse.java
│   └── ErrorResponse.java
│
├── service/
│   ├── PaymentService.java
│   ├── PaymentValidator.java
│   ├── helper/
│   │   ├── CreateOrderHelper.java
│   │   └── CaptureOrderHelper.java
│   └── impl/
│       └── PaymentServiceImpl.java
│
└── util/
    ├── JsonUtil.java
    └── PaypalOrderUtil.java
</pre>

<hr/>

<h2>🧠 High-Level Architecture</h2>

<pre>
          ┌──────────────────────┐
          │  Client / Frontend   │
          └──────────┬───────────┘
                     │ REST API Calls
                     ▼
           ┌────────────────────┐
           │  PaymentController │
           └─────────┬──────────┘
                     │
                     ▼
          ┌────────────────────────────┐
          │     PaymentService         │
          │   (Business Orchestration) │
          └──────────┬─────────────────┘
                     │
      ┌──────────────┼─────────────────────┐
      │              │                     │
      ▼              ▼                     ▼
┌──────────┐  ┌──────────────────┐  ┌────────────────────┐
│ Validator│  │ CreateOrderHelper│  │ CaptureOrderHelper │
└─────┬────┘  └──────────────────┘  └────────┬───────────┘
      │                                      │
      ▼                                      ▼
 ┌────────────────────────────────────────────────────────┐
 │             HttpServiceEngine (Custom HTTP)            │
 └──────────────────────┬─────────────────────────────────┘
                        │
                        ▼
              🌐 PayPal REST API Server
</pre>

<hr/>

<h2>🔐 OAuth Flow (Access Token Generation)</h2>

<pre>
Client → /token  
         ↓
 PaymentService → TokenService  
         ↓
 HttpServiceEngine → PayPal /v1/oauth2/token  
         ↓
 Returns access_token, token_type, expires_in
</pre>

<h3>✔ What Happens Internally?</h3>
<ul>
  <li>Credentials loaded from <code>Constant.java</code></li>
  <li>Base64 encoded Authorization header</li>
  <li>Form data posted to PayPal</li>
  <li>Token parsed into <code>PaypalOauthToken</code></li>
</ul>

<hr/>

<h2>🛒 Create Order Flow</h2>

<pre>
Client → /orders/create  
        ↓
PaymentServiceImpl  
        ↓
CreateOrderHelper → builds OrderRequest  
        ↓
HttpServiceEngine → PayPal /v2/checkout/orders  
        ↓
Returns → Approval Link, Order ID
</pre>

<h3>✔ Build Process</h3>
<ul>
  <li><code>Amount</code> → Controls currency + value</li>
  <li><code>PurchaseUnit</code> → Single purchase entry</li>
  <li><code>ExperienceContext</code> → return/cancel URLs</li>
  <li><code>PaymentSource</code> → defines payment provider</li>
</ul>

<hr/>

<h2>💳 Capture Order Flow</h2>

<pre>
Client → /orders/{id}/capture  
        ↓
PaymentServiceImpl  
        ↓
CaptureOrderHelper  
        ↓
HttpServiceEngine → PayPal /v2/checkout/orders/{id}/capture  
        ↓
Returns → Status, Payer Info, Amount Captured
</pre>

<hr/>

<h2>🛡 Custom Exception Handling</h2>

<h3>🔥 Flow Diagram</h3>

<pre>
Exception Thrown  
       ↓
PaypalProviderException (custom)  
       ↓
GlobalExceptionHandler  
       ↓
ErrorCodeEnum → maps type  
       ↓
ErrorResponse (clean JSON)
</pre>

<h3>✔ Handled Scenarios</h3>
<ul>
  <li>PayPal API connectivity issues</li>
  <li>Validation failures (missing fields)</li>
  <li>Invalid or expired access token</li>
  <li>Unexpected server errors</li>
</ul>

<hr/>

<h2>🧪 Validation Flow</h2>

<pre>
Client Input  
     ↓
PaymentValidator  
     ↓
Success → Helper Layer  
Failure → ErrorCodeEnum → ErrorResponse
</pre>

<hr/>

<h2>🔧 Key Components Explained</h2>

<h3>1️⃣ HttpServiceEngine (Custom HTTP Client)</h3>
<p>
All PayPal API calls are made through this engine.
It handles headers, body mapping, JSON conversion, and error handling.
</p>

<h3>2️⃣ CreateOrderHelper</h3>
<p>Builds complete PayPal order structure using models.</p>

<h3>3️⃣ CaptureOrderHelper</h3>
<p>Handles capture logic + extraction of PayPal capture response.</p>

<h3>4️⃣ JsonUtil</h3>
<p>Central JSON mapper (serialization/deserialization).</p>

<h3>5️⃣ PaypalOrderUtil</h3>
<p>Utility functions for PayPal-specific object transformations.</p>

<hr/>

<h2>📡 API Endpoints</h2>

<h3>🔐 Generate Token</h3>
<pre>POST /api/v1/paypal/token</pre>

<h3>🛒 Create Order</h3>
<pre>POST /api/v1/paypal/orders/create</pre>

<h3>💳 Capture Order</h3>
<pre>POST /api/v1/paypal/orders/{orderId}/capture</pre>

<hr/>

<h2>🛠 Tech Stack</h2>

<ul>
  <li>Java 17</li>
  <li>Spring Boot</li>
  <li>PayPal REST API</li>
  <li>Maven</li>
  <li>Custom HTTP Engine</li>
</ul>

<hr/>

<h2>🔧 Run Locally</h2>

<pre>
git clone https://github.com/ayusseth/paypal-integration.git
cd paypal-integration
mvn clean install
mvn spring-boot:run
</pre>

Update credentials in <code>Constant.java</code>.

<hr/>

## ✨ Author
<p align="center">
  <a href="https://linkedin.com/in/ayuseth" target="_blank">
    <img src="https://readme-typing-svg.herokuapp.com?font=Orbitron&size=24&color=00FFAB&center=true&vCenter=true&width=600&lines=👨‍💻+Ayush+Seth;💡+Computer+Science+Engineer;🚀+Java+Developer" alt="Author Typing Effect" />
  </a>
</p>

<p align="center">
  <a href="https://linkedin.com/in/ayuseth" target="_blank">
    <img src="https://capsule-render.vercel.app/api?type=waving&color=00FFAB&height=100&section=footer&text=Ayush+Seth&fontSize=32&animation=twinkling&fontColor=ffffff" />
  </a>
