#include <Servo.h>
#include <Wire.h>
#include <ESP8266WiFi.h>
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x3f, 16, 4);
#include<FirebaseArduino.h>
#define FIREBASE_HOST "shockwave-1a259.firebaseio.com"
#define FIREBASE_AUTH "zen9z1C4ff09yvZz08Pejo9zRPbWwqk4ebsoXSFD"
#define WIFI_SSID "Taranga"
#define WIFI_PASSWORD "loading"
Servo myservo;
int val1;
void setup()
{
  Serial.begin(115200);
  myservo.attach(3);
  lcd.begin();





  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected:");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setInt("S1", 0);
  Firebase.setInt("S2", 0);


}
void firebasereconnect()
{
  Serial.println("Trying to reconnect");
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}
void loop()
{
  {
    lcd.setCursor(0, 0);
    lcd.print("_____WaveVent_____");
    lcd.setCursor(0, 1);
    lcd.print("Status : Active");
    lcd.setCursor(0, 2);
    lcd.print("Beats  : 10 beats/mi");
    lcd.setCursor(0, 3);
    lcd.print("Volume : 5");
  }
  if (Firebase.failed())
  {
    Serial.print("setting number failed:");
    Serial.println(Firebase.error());
    firebasereconnect();
    return;
  }


  pulse  = Firebase.getString("S1").toInt();
  val1 = Firebase.getString("S2").toInt();
  int del = ( 30000 / pulse);
  int volume = ( vall * 36);
  if (val1 == 1)
  {
    myservo.write(volume);
    delay(del);
    myservo.write(volume);
    delay(del);
    Serial.println("volume");
    Serial.println("del");
  }
}
