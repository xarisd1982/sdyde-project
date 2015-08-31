#include <Wire.h>
#include <BH1750.h>
#include <DHT.h>
#include <SimpleTimer.h>


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
SimpleTimer timer;
int currentValueInt; //the value to send as current value (if light is on we set a current for the system to understand that node1-onD11 switched on the light

DHT dht(DHTPIN, DHTTYPE);


void setup(){
  Serial.begin(9600);
  lightMeter.begin();
  dht.begin();
  pinMode(pir, INPUT);
  pinMode(11, OUTPUT);
  timer.setInterval(5000, measure);
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
    result += "$$node1|";
    result += lux;
    result += "|";
    result.concat(intTemperature);
    result += "|";
    result += motionStatus;
    result += "|";
    result += fsrReading;
    result += "|";
    result.concat(currentValueInt);
    result += "$$";
    Serial.print(result);//for normal use
  }
}

void loop(){
  timer.run();
  if (Serial.available()) {
    String input = Serial.readString();
    
    if (input.indexOf("node1-onD11") > -1) {
        digitalWrite(11,HIGH);
        currentValueInt = 5;
    } else if (input.indexOf("node1-offD11") > -1) {
        digitalWrite(11,LOW);
        currentValueInt = 0;
    }
    input="";
  }
  delay(10);
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

  //Serial.println(result);//for debug use
//  result.concat(lux).concat("|").concat(temperature).concat("|").concat(motionStatus).concat("|");
//  Serial.print(lux);
//  Serial.print("|");
//  Serial.print(temperature);
//  Serial.print("|");
//  Serial.println(motionStatus);
//  Serial.print("|");
  /*Serial.print("Light: ");
  Serial.print(lux);
  Serial.println(" lx");
  
  Serial.print("Temperature: "); 
  Serial.print(temperature);
  Serial.println(" *C ");
  
  Serial.print("Status: ");
  Serial.println(motionStatus);*/

