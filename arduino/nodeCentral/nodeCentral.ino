#include <Wire.h>
#include <BH1750.h>
#include <DHT.h>
#include <SimpleTimer.h>
#include <SoftwareSerial.h>

#define DHTTYPE DHT22   // DHT 22  (AM2302)

//define pins
#define DHTPIN 2
#define pir 3
//##########previous status values##########
uint16_t luxPrevious = -1;
int intTemperaturePrevious = 0;
boolean motionStatusPrevious = false;
int fsrReadingPrevious = 0;
float currentValueFloatPrevious = -1;
//##########previous status values##########
BH1750 lightMeter;//A4-A5
int fsrPin=3; // the FSR and 10K pulldown are connected to a3
int acsPin=2; // the ACS712 is connected to a2
int fsrReading; // the analog reading from the FSR resistor divider
int currentSensorValue; // the analog reading from the ACS712 sensor
SimpleTimer measureTimer;
int currentValueInt; //the value to send as current value (if light is on we set a current for the system to understand that node2-onD11 switched on the light

DHT dht(DHTPIN, DHTTYPE);

SimpleTimer xbeeInputTimer;
SoftwareSerial mySerial(11, 10); // RX, TX this is used to communicate with XBEE. Simple Serial to communicate with Raspberry Pi
String input = "";
String input2 = "";
String raspberryInput = "";

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  lightMeter.begin();
  dht.begin();
  xbeeInputTimer.setInterval(1000, getInputFromXbeeAndSendToRaspberry);
  measureTimer.setInterval(5000, measure);
}

void loop() {
  measureTimer.run();
  xbeeInputTimer.run();
  getInputFromRaspberry();
}
//$$node1|3|26|0|0|0$$$$node2|3|26|0|0|0$$
//node1|3|26|0|0|0$$$$node2|3|26|0|0|0
void getInputFromXbeeAndSendToRaspberry() {
  if (mySerial.available()) {
    input = input + mySerial.readString();
    String temp = stripString(input,"$$");
    if (temp.length() > 0) {
      if (temp.indexOf("node1") >=0 && temp.indexOf("node2") >=0) { //both inputs came together
        if (temp.indexOf("node1") < temp.indexOf("node2")) {
          sendRequestToRaspberryPi(temp.substring(0, temp.indexOf("$$node2")-2));
          sendRequestToRaspberryPi(temp.substring(temp.indexOf("$$node2")));
        } else if (temp.indexOf("node1") > temp.indexOf("node2")) {
          sendRequestToRaspberryPi(temp.substring(0, temp.indexOf("$$node1")-2));
          sendRequestToRaspberryPi(temp.substring(temp.indexOf("$$node1")));
        }  
      } else if (temp.indexOf("node1") >=0) {
        sendRequestToRaspberryPi(temp);
        delay(500);
      } else if (temp.indexOf("node2") >=0) {
        sendRequestToRaspberryPi(temp);
      }
      input = "";
    } else {
      return; 
    }
  }  
}

void getInputFromRaspberry() { //TODO: fix this method. No delays should appear here.
  if (Serial.available()) {
    input2 = Serial.readString();
//Serial.print("1:" + input2);//got: predictionResult:{"label":3.0}
    if (input2.substring(0, 17).equals("predictionResult:")) { //raspberry sends predictionResult:{"label":3.0}
      raspberryInput=input2.substring(26, 27);
//Serial.print("2222##" + raspberryInput + "##2222");
      if (raspberryInput.equals("1")) {
//        Serial.print("#######1#######");
        delay(500);mySerial.print("$node1-offD11$");delay(500);mySerial.print("$node2-offD11$");digitalWrite(12, LOW);
      } else if (raspberryInput.equals("2")) {
//        Serial.print("#######2#######");
        delay(500);mySerial.print("$node1-onD11$");delay(500);mySerial.print("$node2-offD11$");digitalWrite(12, LOW);
      } else if (raspberryInput.equals("3")) {
//        Serial.print("#######3#######");
        delay(500);mySerial.print("$node1-offD11$");delay(500);mySerial.print("$node2-onD11$");digitalWrite(12, LOW);       
      } else if (raspberryInput.equals("4")) {
//        Serial.print("#######4#######");        
        delay(500);mySerial.print("$node1-offD11$");delay(500);mySerial.print("$node2-offD11$");digitalWrite(12, HIGH);
      } else if (raspberryInput.equals("5")) {
        delay(500);mySerial.print("$node1-onD11$");delay(500);mySerial.print("$node2-onD11$");digitalWrite(12, LOW);
      } else if (raspberryInput.equals("6")) {
        delay(500);mySerial.print("$node1-onD11$");delay(500);mySerial.print("$node2-offD11$");digitalWrite(12, HIGH);
      } else if (raspberryInput.equals("7")) {
        delay(500);mySerial.print("$node1-offD11$");delay(500);mySerial.print("$node2-onD11$");digitalWrite(12, HIGH);
      } else if (raspberryInput.equals("8")) {
        delay(500);mySerial.print("$node1-onD11$");delay(500);mySerial.print("$node2-onD11$");digitalWrite(12, HIGH);
      } else {
        delay(500);mySerial.print("$node1-offD11$");delay(500);mySerial.print("$node2-offD11$");digitalWrite(12, LOW); 
      }
    }
    input2 = "";
  }
}

/**
This method strips a string from starting and ending special characters.
Special characters are put as arguments. In case of wrong input it returns an empty String
e.g. inputString="!!someInput!!" , specialString="!!" returns
"someInput"
*/
String stripString(String inputString, String specialString) {
  String result;
  if (inputString.length() > 0 && specialString.length()>0 && (2*specialString.length())<inputString.length()) {
    if (inputString.indexOf(specialString) >= 0
        && inputString.substring(specialString.length()).indexOf(specialString) >= 0) {
      result = inputString.substring(specialString.length(),inputString.length()-specialString.length());
    }
  }
    return result;
}

void sendRequestToRaspberryPi(String stringToSend) {
  String raspOut = "$$" + stringToSend + "$$";
  Serial.print(raspOut);
}

// a function to be executed periodically
void measure() {
  //Serial.println("start measure...");
  uint16_t lux = lightMeter.readLightLevel();
  float floatTemperature = dht.readTemperature(false);
  delay(250);
  int intTemperature = round(floatTemperature);
  boolean motionStatus = false;
  motionStatus = digitalRead(pir);
  fsrReading = analogRead(fsrPin);
  currentSensorValue = analogRead(acsPin);
  float currentValueFloat = (512-(float)currentSensorValue)*75.75757576/1023;
//  currentValueInt = round(currentValueFloat); //normally this is the real value of the sensor
  //Serial.print("lux: ");  Serial.println(lux);
  //Serial.print("intTemperature: ");  Serial.println(intTemperature);
  //Serial.print("motionStatus: ");  Serial.println(motionStatus);
  //Serial.print("fsrReading: ");  Serial.println(fsrReading);
  //Serial.print("currentValueInt: ");  Serial.println(currentValueInt);
  if (hasStatusChanged (lux, intTemperature, motionStatus, fsrReading, currentValueFloat)) {
    //Serial.println("?????enter??????");
    luxPrevious = lux;
    intTemperaturePrevious = intTemperature;
    motionStatusPrevious = motionStatus;
    fsrReadingPrevious = fsrReading;
    currentValueFloatPrevious = currentValueFloat;
    String result = String();
    result += "node3|";
    result += lux;
    result += "|";
    result.concat(intTemperature);
    result += "|";
    result += motionStatus;
    result += "|";
    result += fsrReading;
    result += "|";
    result.concat(currentValueInt);
    sendRequestToRaspberryPi(result);
  }
}

boolean hasStatusChanged (uint16_t lux, int intTemperature, boolean motionStatus, int fsrReading, float currentValueFloat) {
  boolean result = false;
  if (luxPrevious==-1 && intTemperaturePrevious==0 && motionStatusPrevious==false && fsrReadingPrevious==0 && currentValueFloatPrevious==-1) { //first time
     //Serial.println("first time");
     result = true;
  }
  if (
      (abs(lux-luxPrevious)>50 && abs((lux-luxPrevious)*100/luxPrevious)>10)
      || (abs(intTemperature-intTemperaturePrevious)>2 && abs((intTemperature-intTemperaturePrevious)*100/intTemperaturePrevious)>10)
      || (motionStatusPrevious != motionStatus)
      || (abs(fsrReading-fsrReadingPrevious)>50 && abs((fsrReading-fsrReadingPrevious)*100/fsrReadingPrevious)>10)
      || ((currentValueFloatPrevious==0 || currentValueFloat==0) && currentValueFloatPrevious!=currentValueFloat)
     ) {
       //Serial.println("status has changed!!!");
       result = true; 
     }     
    return result;
}
