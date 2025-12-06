<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="ISO-8859-1" />
        <title>Payment Confirmation</title>
        <link
            rel="stylesheet"
            href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
        />
        <style>
            .inlineimage {
                max-width: 470px;
                margin-right: 8px;
                margin-left: 10px;
            }

            .images {
                display: inline-block;
                max-width: 98%;
                height: auto;
                width: 22%;
                margin: 1%;
                left: 20px;
                text-align: center;
            }
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                margin: 0;
                padding: 0;
            }
            .container {
                width: 80%;
                margin: 0 auto;
                padding: 20px;
                background-color: #fff;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                margin-top: 50px;
            }
            .btn {
                display: block;
                width: 100%;
                padding: 15px;
                background-color: #1677ff;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 18px;
                transition: background-color 0.3s;
            }
            .btn:hover {
                background-color: #4096ff;
            }
            .booking-info {
                background-color: #f9f9f9;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
            }
            .booking-info h3 {
                margin-top: 0;
                color: #333;
            }
            .payment-method {
                text-align: center;
                margin-bottom: 30px;
            }
            .payment-method img {
                height: 60px;
                margin: 10px;
            }
        </style>
        <link
            type="text/css"
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/UserHome_Css.css"
        />
    </head>
    <body>
        <div class="hd">
            <h1>Payment Confirmation</h1>
        </div>

        <header>
            <h1 class="hd">National Ticket Booking Spot UH</h1>

            <!-- Home Hyperlik -->
            <div class="home">
                <p class="menu">
                    <a href="#">Home</a>
                </p>
            </div>

            <!-- View All trains Hyperink-->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/viewAllTrains"
                        >View All Trains</a
                    >
                </p>
            </div>

            <!-- Find Trains between 2 staions Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/findTrainsbetweenStaionsFwd"
                        >Trains between staions</a
                    >
                </p>
            </div>

            <!-- Diplay train booking History Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/showTicketBookingHistory"
                        >Ticket Booking History</a
                    >
                </p>
            </div>

            <!-- train fair Enquery Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/trainFairEnqueryFwd"
                        >Fair Enquery</a
                    >
                </p>
            </div>

            <!-- Check Seats Availability Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/trainSeatsAvailablityCheckFwd"
                        >Check Seats Availability</a
                    >
                </p>
            </div>

            <!-- Search a train using train Number Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/user/searchTrainByNumberFwd"
                        >Search Train By Number</a
                    >
                </p>
            </div>

            <!-- display user profile Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a
                        href="${pageContext.request.contextPath}/userProfile/view"
                        >Profile</a
                    >
                </p>
            </div>

            <!-- user Logout Hyperlink -->
            <div class="home">
                <p class="menu">
                    <a href="${pageContext.request.contextPath}/logout"
                        >Logout</a
                    >
                </p>
            </div>
        </header>

        <div class="container">
            <h1>Payment Confirmation</h1>

            <div class="booking-info">
                <h3>Booking Details</h3>
                <p><strong>Train Number:</strong> ${trainDTO.trainNo}</p>
                <p><strong>From:</strong> ${trainDTO.fromStation}</p>
                <p><strong>To:</strong> ${trainDTO.toStation}</p>
                <p>
                    <strong>Date of Journey:</strong> ${ticketDTO.journeyDate}
                </p>
                <p><strong>Seat Type:</strong> ${ticketDTO.seatType}</p>
                <p>
                    <strong>Seats Required:</strong> ${ticketDTO.seatsRequired}
                </p>
            </div>

            <form
                action="${pageContext.request.contextPath}/user/confirmTrainBooking"
                method="post"
            >
                <!-- Hidden fields for train details -->
                <input
                    type="hidden"
                    name="trainNo"
                    value="${trainDTO.trainNo}"
                />
                <input
                    type="hidden"
                    name="fromStation"
                    value="${trainDTO.fromStation}"
                />
                <input
                    type="hidden"
                    name="toStation"
                    value="${trainDTO.toStation}"
                />

                <input
                    type="hidden"
                    name="journeyDate"
                    value="${ticketDTO.journeyDate}"
                />
                <input
                    type="hidden"
                    name="seatsRequired"
                    value="${ticketDTO.seatsRequired}"
                />
                <input
                    type="hidden"
                    name="seatType"
                    value="${ticketDTO.seatType}"
                />

                <div class="payment-method">
                    <h3>Payment Method</h3>
                    <p>
                        You will be redirected to Alipay to complete your
                        payment
                    </p>
                    <img
                        src="https://img.alicdn.com/imgextra/i3/O1CN01YHf1uW1mKf2wX6J4a_!!6000000004732-2-tps-200-200.png"
                        alt="支付宝"
                    />
                </div>

                <button type="submit" class="btn">
                    Proceed to Pay with Alipay
                </button>
            </form>
        </div>
    </body>
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</html>
