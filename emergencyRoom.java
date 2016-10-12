// Daniel Hernandez
// 12/3/15
// CSC 311
// Project 5

/*
    Project 5: This program implements the scheduling of patients in an Emergency
    Room by using a Priority Queue as required in the Project 5 PDF file and follows
    all directions and instructions. This program is menu driven with the options
    to check in a patient, attend the next patient, output a list of patients that
    have been attended based on priority and an option to end the program. Patient
    data is placed onto the Priority Queue based on urgency. The patient with highest
    urgency gets attended next.
*/

//Required import
import java.util.*;

public class emergencyRoom
{
    public static void main(String[] args) // begin main() method [5 lines]
    {
        emergencyRoom ERSystem = new emergencyRoom(); // create Object from class
        
        ERSystem.showMenu(); // start the menu
    } // end main
    
    /*
        Displays the menu to the user. Accepts the user's input and if the input
        is valid, that option will be executed from the switch-case block. If
        an invalid option is entered, the menu will be displayed again and the
        user will be asked for input again. This repeats until the input is
        valid.
    */
    public void showMenu() // begin showMenu() method
    {
        Scanner input = new Scanner(System.in); // Used to take input
        
        System.out.println("Select an option by entering its corresponding number" + "\n"
                         + "1. Check in a patient." + "\n"
                         + "2. Attend the next patient." + "\n"
                         + "3. Output log of patients attended." + "\n"
                         + "4. End the program.");
        
        System.out.print("> ");
        
        // test the input for a valid number entered.
        try
        {
            optionEntered = input.nextInt();
            System.out.println();
        }
        catch (InputMismatchException e)
        {
            // Do nothing.
        }
        
        // uses the number entered to execute the correct option or default case
        // for invalid input.
        switch(optionEntered)
        {
            case 1:
            {
                patientCheckIn(); // Option 1: Check in a patient
                break;
            }
            case 2:
            {
                attendPatient(); // Option 2: Attend the next patient.
                break;
            }
            case 3:
            {
                printPatientVisitLog(); // Option 3: Output list of patients attended.
                break;
            }
            case 4:
            {
                System.out.println("Ending program."); // Option 4: End the program
                break;
            }
            default:
            {
                System.out.println("\nInvalid option entered. Please try again.\n");
                showMenu(); // Any invalid numbers will display the menu again.
                break;
            }
        }
    } // end showMenu
    
    /*
        Method printPatientVisitLog() outputs the list of patients that have
        already been attended by order of priority.
    */
    public void printPatientVisitLog() // begin printPatientVisitLog() method
    {
        // check if patients have beeen attended yet.
        if(emergencyRoom.arrayLogPos == 0)
        {
            System.out.println("No patients have been attended in this time frame.");
        }
        else
        {
            System.out.println("Here is the order in which the patients were attended for this time frame.");
            System.out.println();
            
            // prints out the details of the patients in order attended based on
            // their priority in the Queue.
            for(int i = 0; i < emergencyRoom.arrayLogPos; i++)
            {
                System.out.println("Patient's Name: " + emergencyRoom.patientVisitLog[i].firstName + " " + emergencyRoom.patientVisitLog[i].lastName + "\n"
                               + "Patient's Date of Birth: " + emergencyRoom.patientVisitLog[i].dateOfBirth + "\n"
                               + "Patent's Medical Condition: " + emergencyRoom.patientVisitLog[i].medicalCondition + "\n"
                               + "Arrival Time: " + emergencyRoom.patientVisitLog[i].arrivalTime + "\n"
                               + "Time Attended: " + emergencyRoom.patientVisitLog[i].timeAttended + "\n"
                               + "Urgency: " + emergencyRoom.patientVisitLog[i].urgency + "\n");
            }
        }
        
        showMenu(); // display menu
    } // end printPatientVisitLog
    
    /*
        method attendPatient() takes the next patient from the priority queue,
        adds the time they are attended to the data field timeAttended, and then
        patient data is logged to a regular array which is used in the printPatientVisitLog
        method.
    */
    public void attendPatient() // begin attendPatient() method
    {
        Patient patientAttended = priorityDequeue(); // Get the next patient form the Priority Queue
        
        // checks if priority queue is empty.
        if(patientAttended == null)
        {
            System.out.println("No patients in the queue to attend.\n");
        }
        else
        {
            // checks if this method is being run again repeatedly
            if(emergencyRoom.patientAttendRunAgain == false)
            {
                emergencyRoom.minuteAttend = emergencyRoom.minuteArriveTrace + emergencyRoom.minuteTrace;
            }
            else
            {
                emergencyRoom.minuteTrace = emergencyRoom.minuteTrace + 2;
                emergencyRoom.minuteAttend = emergencyRoom.minuteArriveTrace + emergencyRoom.minuteTrace;
            }
            
            // The next three if statements deal with the formating of the time, as a string value.
            if(emergencyRoom.minuteAttend < 10)
            {
                patientAttended.timeAttended = "8:0" + Integer.toString(emergencyRoom.minuteAttend) + " PM";
            }
            
            if(emergencyRoom.minuteAttend >= 10 && emergencyRoom.minuteAttend < 60)
            {
                patientAttended.timeAttended = "8:" + Integer.toString(emergencyRoom.minuteAttend) + " PM";
            }
            
            if(emergencyRoom.minuteAttend == 60)
            {
                patientAttended.timeAttended = "9:00 PM";
            }
            
            //This message displays to indicate a patient was taken from the Priority Queue and attended.
            System.out.println("The patient has been attended.\n");
            
            // Simulation will only log the first 15 patients.
            if(emergencyRoom.arrayLogPos < 15)
            {
                emergencyRoom.patientVisitLog[emergencyRoom.arrayLogPos] = patientAttended;
                emergencyRoom.arrayLogPos = emergencyRoom.arrayLogPos + 1;
            }
            else
            {
                System.out.println("Simulation will only log 15 patients, patient info not added to log for viewing.");
            }
        }
        
        emergencyRoom.patientAttendRunAgain = true; // indicate this method has been run.
        showMenu(); // display menu
    } // end attendPatient
    
    /*
        Method patientCheckIn() will ask the user input the required information about
        the patient. For this simulation, the time is 8PM and will simulate 1 hour of time.
        Once the information is entered, the current arrivalTime is recorded, then this time
        is incrimented randomly by 0, 1, or 2 minutes for the next arriving patient.
    */
    public void patientCheckIn() // begin patientCheckIn() method
    {
        // We are simulating 15 patients in the Priority Queue at most.
        if(emergencyRoom.arrayPos == 15)
        {
            System.out.println("Max limit reached: There are 15 patients in the queue. Consider attending them.");
        }
        else
        {
            // Required varieables for the patient
            String firstName;
            String lastName;
            String dateOfBirth;
            String medicalCondition;
            String arrivalTime = null;
            String timeAttended = null;
            int urgency;
            
            // Random number generator for passage of time
            Random rng = new Random();
            
            Scanner input = new Scanner(System.in);
            
            //Take input for required data
            System.out.println("Please enter the patient's first name.");
            firstName = input.nextLine();
            
            System.out.println("Please enter the patient's last name.");
            lastName = input.nextLine();
            
            System.out.println("Please enter the patient;s date of birth (In the format: MM/DD/YY).");
            dateOfBirth = input.nextLine();
            
            System.out.println("Please enter the medical condition of the patient.");
            medicalCondition = input.nextLine();
            
            System.out.println("Please enter a priority number for the patient (1 being the highest and 20 the lowerst).");
            urgency = input.nextInt();
            
            // Formatting for time, as a String
            if(emergencyRoom.minuteArrive < 10)
            {
                arrivalTime = "8:0" + Integer.toString(emergencyRoom.minuteArrive) + " PM";
            }
            
            if(emergencyRoom.minuteArrive >= 10 && emergencyRoom.minuteArrive < 60)
            {
                arrivalTime = "8:" + Integer.toString(emergencyRoom.minuteArrive) + " PM";
            }
            
            if(emergencyRoom.minuteArrive == 60)
            {
                arrivalTime = "9:00 PM";
            }
            
            emergencyRoom.minuteArriveTrace = emergencyRoom.minuteArrive; // keep the current arrival time
            emergencyRoom.minuteArrive = emergencyRoom.minuteArrive + rng.nextInt(3); // passage of time by 0, 1, or 2 minutes
            
            // Create Patient object using class Patient
            Patient patient = new Patient(firstName, lastName, dateOfBirth, medicalCondition, arrivalTime, timeAttended, urgency);
            
            priorityEnqueue(patient); // insert the Patient object into the priority Queue
        }
        
        emergencyRoom.patientAttendRunAgain = false; // indicates that method attendPatient was not run, thus time has changed.
        
        System.out.println();
        showMenu(); // display menu
    } // end patientCheckIn
    
    /*
        Method priorityEnqueue() follows the heap insert algorith form the slides 
        to insert a Patient into the priority queue based on urgency. This method
        takes in a Patient object. ** This method was created by following the 
        algorithm from the slides about heap insert.**
    */
    public void priorityEnqueue(Patient patient) // begin priorityEnqueue(0 method
    {
        int child = emergencyRoom.arrayPos;
        int parent = (child - 1) / 2;
        
        emergencyRoom.priorityQueue[child] = patient;
        emergencyRoom.arrayPos = emergencyRoom.arrayPos + 1;
        
        while((parent >= 0) && (emergencyRoom.priorityQueue[parent].urgency > emergencyRoom.priorityQueue[child].urgency))
        {
            Patient temp = emergencyRoom.priorityQueue[parent];
            
            emergencyRoom.priorityQueue[parent] = emergencyRoom.priorityQueue[child];
            emergencyRoom.priorityQueue[child] = temp;
            
            child = parent;
            parent = (child - 1) / 2;
        }
    } //end priorityDequeue
    
    /*
        Method priorityDequeue() removes the next Patient object from the Priority Queue
        and returns this Patient object. ** This method was created by following the
        algorithm found in the slides for heap removal.**
    */
    public Patient priorityDequeue() // begin priorityDequeue() method
    {
        int parent = 0;
        int leftChild = 0;
        int rightChild = 0;
        
        // Check if the priority queue is empty.
        if(emergencyRoom.arrayPos == 0)
        {
            return null;
        }
        else
        {
            Patient removePatient = emergencyRoom.priorityQueue[0];
            emergencyRoom.priorityQueue[0] = emergencyRoom.priorityQueue[emergencyRoom.arrayPos - 1];
            emergencyRoom.priorityQueue[emergencyRoom.arrayPos - 1] = null;
            emergencyRoom.arrayPos = emergencyRoom.arrayPos - 1;
        
            while(true)
            {
                leftChild = (2 * parent) + 1;
                rightChild = leftChild + 1;
                
                if(leftChild >= emergencyRoom.arrayPos)
                    break;
                
                int minChild = leftChild;
                
                if ((rightChild < emergencyRoom.arrayPos) && (emergencyRoom.priorityQueue[rightChild].urgency < emergencyRoom.priorityQueue[leftChild].urgency))
                {
                    minChild = rightChild;
                }
                
                if(emergencyRoom.priorityQueue[parent].urgency > emergencyRoom.priorityQueue[minChild].urgency)
                {
                    Patient temp = emergencyRoom.priorityQueue[parent];
                    
                    emergencyRoom.priorityQueue[parent] = emergencyRoom.priorityQueue[minChild];
                    emergencyRoom.priorityQueue[minChild] = temp;
                }
                else
                    break;
            }
            
            return removePatient;
        }
    } // end priorityDequeue
    
    /*
        Class patient is created according to the directions in the Project 5
        PDF file. This class has the constructor Patient that creates the Patient
        object with the required data.
    */
    class Patient
    {
        // constructor
        public Patient(String fN, String lN, String DOB, String medCond, String timeArrive, String timeAttend, int priority)
        {
            firstName = fN;
            lastName = lN;
            dateOfBirth = DOB;
            medicalCondition = medCond;
            arrivalTime = timeArrive;
            timeAttended = timeAttend;
            urgency = priority;
        }
        
        // Required data for the patient object.
        String firstName;
        String lastName;
        String dateOfBirth;
        String medicalCondition;
        String arrivalTime;
        String timeAttended;
        int urgency;
    }
    
    // Required variables for this program.
    private static Patient[] priorityQueue = new Patient[15];
    private static Patient[] patientVisitLog = new Patient[15];
    private int optionEntered;
    private static int arrayLogPos = 0;
    private static int arrayPos = 0;
    private static int minuteArrive = 0;
    private static int minuteAttend = 0;
    private static int minuteTrace = 0;
    private static int minuteArriveTrace = 0;
    private static boolean patientAttendRunAgain;
}