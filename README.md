# Little Pay Coding Exercise (Calculate Fare)

## Instruction received from Little Pay Engineering Team

* [Java Coding Exercise]
  * [Introduction](#introduction-)
  * [The problem](#the-problem)
  * [Assumptions](#assumptions)
  * [Execution](#execution)

<!-- TOC -->

## Introduction 
Littlepay is a cloud service that enables transit companies to charge their passengers for their travel by credit card. In order to do this at scale, we use a pretty sophisticated tech stack and have to solve some pretty interesting problems. To keep this exercise small so we don’t take too much of your time, you will be building a hypothetical system that uses some highly simplified concepts from our system.
How the hypothetical system works

Before boarding a bus at a bus stop, passengers tap their credit card (identified by the PAN, or Primary Account Number) on a card reader. This is called a tap on. When the passenger gets off the bus, they tap their card again. This is called a tap off. The amount to charge the passenger for the trip is determined by the stops where the passenger tapped on and tapped off. The amount the passenger will be charged for the trip will be determined as follows:
Trips between Stop 1 and Stop 2 cost $3.25 Trips between Stop 2 and Stop 3 cost $5.50 Trips between Stop 1 and Stop 3 cost $7.30

Note that the above prices apply for travel in either direction (e.g. a passenger can tap on at stop 1 and tap off at stop 2, or they can tap on at stop 2 and can tap off at stop 1. In either case, they would be charged $3.25).

### Completed Trips
If a passenger taps on at one stop and taps off at another stop, this is called a complete trip. The amount the passenger will be charged for the trip will be determined based on the table above. For example, if a passenger taps on at stop 3 and taps off at stop 1, they will be charged $7.30.

### Incomplete trips
If a passenger taps on at one stop but forgets to tap off at another stop, this is called an incomplete trip. The passenger will be charged the maximum amount for a trip from that stop to any other stop they could have travelled to. For example, if a passenger taps on at Stop 2, but does not tap off, they could potentially have travelled to either stop 1 ($3.25) or stop 3 ($5.50), so they will be charged the higher value of $5.50.

### Cancelled trips
If a passenger taps on and then taps off again at the same bus stop, this is called a cancelled trip. The passenger will not be charged for the trip.

## The problem
Given an input file in CSV format containing a single tap on or tap off per line you will need to create an output file containing trips made by customers.
taps.csv [input file]

You are welcome to assume that the input file is well formed and is not missing data. 
### Example input file:
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559 2, 22-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559
trips.csv [output file]

You will need to match the tap ons and tap offs to create trips. You will need to determine how much to charge for the trip based on whether it was complete, incomplete or cancelled and where the tap on and tap off occurred. You will need to write the trips out to a file called trips.csv.

### Example output file:

Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
22-01-2018 13:00:00, 22-01-2018 13:05:00, 900, Stop1, Stop2, $3.25, Company1, B37, 5500005555555559, COMPLETED

Important: Do not use real credit card numbers (PANs) in the test data you provide to us.
You can find credit card numbers suitable for testing purposes at http://support.worldpay.com/support/kb/bg/testandgolive/tgl5103.html
As part of your solution:
- List any assumptions that you made in order to solve this problem
- Provide instructions on how to run the application
- Provide a test harness to validate your solution
  If you are pressed for time we suggest you focus on the business logic around matching and pricing taps over reading/writing CSV.
  Thank you and have fun!

## Assumptions
- If Input file contains less than expected fields remaining processing of file will continue with logger message 
  for fewer fields row. However, based on business requirement this can be handled.
- For cancel case assuming even if tap off happens after more than 5 min, then also no fare would be charged with
  current implementation, however in my opinion in real time scenario we can potentially improve the system by 
  adding this condition say if time > 5 min then charge maximum fare based on business requirement decision.  
- Assuming there is no Off without On, however this case can be handled for the INCOMPLETE trip. 
- Assuming at a time there is one Tap On and Off, no consecutive Tap On and tap Off. 

## Execution
1. First, clone the repository from GitHub to your local machine using git clone:
  git clone https://github.com/parul6/littlepay-coding_exercise
2. Navigate to project directory

   cd littlepay-coding_exercise
3. Ensure gradle is installed with the command gradle -v
4. Build the project using ./gradlew build, On windows use gradlew.bat build
5. Run the application using command ./gradlew run
6. After running the application output .csv file will be generated at path "littlepay-coding_exercise/build/output" 
   with the name trips.csv.
7. Input file (taps.csv) is located at "littlepay-coding_exercise/src/main/resources"
8. To run the test cases use command ./gradlew test and on windows use gradlew.bat test

