<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <script type="text/javascript">
        window.onload = function() {
            var dataPoints = [];

            var chart = new CanvasJS.Chart("chartContainer", {
                animationEnabled: true,
                zoomEnabled: true,
                title: {
                    text: "Total Donations by Program"
                },
                axisX: {
                    title: "Program",
                    interval: 1
                },
                axisY: {
                    title: "Total Donations (in USD)"
                },
                data: [{
                    type: "column",
                    xValueFormatString: "Program #",
                    yValueFormatString: "$#,##0.00",
                    dataPoints: dataPoints
                }]
            });

            // Fetch the donation data from backend
            $.getJSON("/total-donations-by-program", function(data) {
                var programId = 1;
                // Iterate through the data to push to the dataPoints for chart
                $.each(data, function(key, value) {
                    dataPoints.push({
                        x: programId++,  // Program ID
                        y: value          // Total donations for the program
                    });
                });

                chart.render();
            });
        }
    </script>
</head>
<body>
<div id="chartContainer" style="height: 370px; width: 100%;"></div>
<script src="https://canvasjs.com/assets/script/jquery-1.11.1.min.js"></script>
<script src="https://cdn.canvasjs.com/canvasjs.min.js"></script>
</body>
</html>
