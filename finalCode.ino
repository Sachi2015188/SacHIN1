#include "UbidotsMicroESP8266.h"
#include "DHT.h"
#define DHTPIN D4    // what digital pin we're connected to dht11 sensor
#define DHTTYPE DHT11   // DHT 11 
DHT dht(DHTPIN, DHTTYPE);
#define humidity "587f6d1476254202bf44924d"  //  Ubidots variable ID humidity
#define temparature  "587f6d0676254202b4b6dc69"  //  Ubidots variable ID temparature
#define relay "587f6d8a7625424ff40f1dd1"  //  Ubidots variable ID relay
#define UbidotsAccessKey "kh3gK7Rcy1VWkEsIwJIMRv2pIHNK4o"  // Ubidots reference api token
#define WIFISSID "Dialog 4G"//wifi connection ssid
#define PASSWORD "TR1N9NMQ5N0"//wifi password
#define RELAY D2// modemcu pin connected to 5v relay

Ubidots ubiclient(UbidotsAccessKey);
void setup()
{
    Serial.begin(115200);
    dht.begin();
    delay(10);
    pinMode(RELAY, OUTPUT);
    ubiclient.wifiConnection(WIFISSID, PASSWORD);
}
void loop(){
    delay(2000);
    float h = dht.readHumidity();
    float t = dht.readTemperature();//read temparature
   Serial.println(t);
   if (isnan(h) || isnan(t)) {
        Serial.println("Failed to read from DHT sensor!");
        return;
    }
   // send temparature humidity values to ubiots platform
    ubiclient.add(humidity, h);
    ubiclient.add(temparature, t);
  
    ubiclient.sendAll();
    float setRelay = ubiclient.getValue(relay);
    if (setRelay == 1.00) {
       Serial.println(setRelay);
        digitalWrite(RELAY, HIGH); //On relay
    }
    else if (setRelay == 0.00) {
       Serial.println(setRelay);
        digitalWrite(RELAY, LOW); //Off relay
    }
}
