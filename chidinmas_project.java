import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//class for storing student data
class Student {
    public String name;
    public int score;
}

class chidinmas_project {
    static ArrayList<Student> board = new ArrayList();//list of students on the scoreboard
    static Scanner in = new Scanner(System.in);//scanner for reading user input

    //loading data from xml document into array list
    static void readDocument(Document doc){
        NodeList nodeList = doc.getElementsByTagName("student");//get list of students from document
        for(int i = 0; i < nodeList.getLength(); i++){//for each student...
            Node node = nodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element e = (Element) node;
                //create a new student object
                Student s = new Student();
                //load data from student in document
                s.name = e.getElementsByTagName("name").item(0).getTextContent();
                try{
                    s.score = Integer.parseInt(e.getElementsByTagName("score").item(0).getTextContent());
                }catch(Exception exception){//if number couldn't be read
                    s.score = 0;
                    System.out.println("Error: " + s.name + "'s score couldn't be read");
                }
                //add a student to the scoreboard
                board.add(s);
            }
        }
    }

    //load data from array list into xml document
    static void writeDocument(Document doc){
        //create "board" tag to wrap list of students
        Element rootElement = doc.createElement("board");
        doc.appendChild(rootElement);
        for(int i = 0; i < board.size(); i++){//for each student...
            Element e = doc.createElement("student");//create xml student object
            //give it a "name" tag and assign name from array list
            Element name = doc.createElement("name");
            name.setTextContent(board.get(i).name);
            e.appendChild(name);
            //give it a "score" tag and assign score from array list
            Element score = doc.createElement("score");
            score.setTextContent(Integer.toString(board.get(i).score));
            e.appendChild(score);
            rootElement.appendChild(e);//add this student to xml document
        }
    }

    //function to add new student
    static void register(){
        Student s = new Student();//create new student
        System.out.println("What is the new student's name? ");
        s.name = in.nextLine();//assign name from user input
        System.out.println("What is the new student's score? ");
        try{
            s.score =Integer.parseInt(in.nextLine());//assign score from user input
        } catch(Exception e){
            System.out.println("Error. Not a number.");//in case user doesn't enter a number
            return;
        }
        board.add(s);//add student to scoreboard
        System.out.println("Student registered.");
    }

    static void list(){
        if(board.size() == 0){//in case there are no students
            System.out.println("Nothing to print");
        }
        for(int i = 0; i <  board.size(); i++){//print all students
            System.out.println(board.get(i).name + " - " + board.get(i).score);
        }
    }

    //linear search for students (by name)
    static int indexOf(String x){
        for(int i = 0; i <  board.size(); i++){//for each student...
            if(board.get(i).name.equals(x)){//if students matches the search name
                return i;
            }
        }
        return -1;//if no match was found
    }

    //change a students score
    static void modify(){
        System.out.println("What is the student's name? ");
        int i = indexOf(in.nextLine());//search for student
        if(i == -1){//if no student found with this name
            System.out.println("Error. Student not found.");
            return;
        }
        System.out.println("What is the student's new score? ");
        try{
            board.get(i).score = Integer.parseInt(in.nextLine());//assign the new score from users input
        } catch(Exception e){
            System.out.println("Error. Not a number.");//in case user doesn't enter a number
            return;
        }
        System.out.println("Score modified.");
    }

    //delete a student
    static void delete(){
        System.out.println("What is the student's name? ");
        int i = indexOf(in.nextLine());//search for student
        if(i == -1){//if no student found with this name
            System.out.println("Error. Student not found.");
            return;
        }
        board.remove(i);//remove student from scoreboard
        System.out.println("Student deleted.");
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        //load document from xml file (if it exists)
        File file = new File("project.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if(file.exists()){//check if the file exists
            Document doc = db.parse(file);//parsed file into document
            doc.getDocumentElement().normalize();
            readDocument(doc);//load data from document into array list
        }
        System.out.println("Welcome to the Student Score Keeping App.");
        while(true){
            System.out.println("");
            System.out.println("CHOOSE AN ACTION:");
            System.out.println(" - (R)egister");
            System.out.println(" - (L)ist");
            System.out.println(" - (M)odify");
            System.out.println(" - (D)elete");
            System.out.println(" - (E)xit");
            String inp = in.nextLine();//user enters action
            System.out.println("");
            if(inp.equals("E") || inp.equals("e")){//exiting the application
                System.out.println("Leaving the application. In 3, 2, 1.");
                break; //exit the loop and end application
            }
            if(inp.equals("R") || inp.equals("r")){//command for adding
                register();
            }else if(inp.equals("L") || inp.equals("l")){//command for listing
                list();
            }else if(inp.equals("M") || inp.equals("m")){//command for updating
                modify();
            }else if(inp.equals("D") || inp.equals("d")){//command for deleting
                delete();
            }else{
                System.out.println("Not an action.");//otherwise
            }
        }
        Document doc2 = db.newDocument();//create new xml document
        writeDocument(doc2);//load data from array list into xml document
        //save document as xml file
        DOMSource source = new DOMSource(doc2);
        StreamResult result = new StreamResult(file);
        TransformerFactory transformerFactory =TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, result);
    }
}