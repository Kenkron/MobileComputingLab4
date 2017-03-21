README.txt
----------------------------------------------------------------------------------------------------------------------------
Description what you did, what worked, what did not. Any extras,
any challenges, and anything else you want to communicate.

What We Did:
This Lab 4 had several components that tied into each other for the final lab result. What we did was provide the webpage with Node.js capabilities with a REST API on one end connecting to the Android application and MQTT on the other connecting to the MBED board.
Since we do not know the location of our beacons, we created a 10x10 grid with circles that pop up when a beacon has at least one mobile device in its range. The circle first appears green until it reaches its maximum size, at which point the circle turns red. For each student entering and exiting the beacon’s range, the circle will grow or shrink respectively.

Challenges We Faced:
There was a lot of difficulty experienced with being able to detect our beacons from the Android application. We tried 3 different methods of implementing beacon detection to correctly depict the population density around that specific beacon. These were:
-Google's Nearby API (using Nearby.Messages.subscribe)
-Android Beacon Library
-BlueCats API (I'm using a BlueCats beacon)
After days of unsuccessful efforts, we discovered a comment within the code stating:
	//Note: On Samsung devices, the connection must be done on main thread
This led us to our breakthrough solution.
*INSERT FINAL SOLUTION*

Execution Shots:

Student Enters Area:
*INSERT SCREENSHOTS OF MQTT TRACKER AND DEBUG OUTPUT*

Student Exits Area:
*INSERT SCREENSHOTS OF MQTT TRACKER AND DEBUG OUTPUT*

