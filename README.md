
# LetterBox App ( Binary Bombers )

An android application for college students loaded with various features to help students such as follows:

    1. Complaint Addressing system
    2. Notice boards
    3. Campus Maps
    4. Mess Management
    5. Health Care
    6. Club/Sports Details
    7. Social Media redirecting
    8. Student Image Recognition
    9. Library Occupancy
    10. Lost & Found



## App

![Application logo](https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/Application%20Logo%2FMAIN%20LOGO%20shadow%20design.png?alt=media&token=d8bdb1a2-e9c1-4baf-89ce-3cbd079ad49c "Application Logo")

[Download Application Apk from here](https://drive.google.com/file/d/1FATGvXwtR_-r_WpMBNYdij4mhAqttO_g/view?usp=sharing)

# 


The detailed of every feature can be viewed from the following link:

[Presentation Link for LetterBox App](https://drive.google.com/file/d/1anl-dX30krNLPDplzVcc2JH_58ijJt0y/view?usp=sharing)

[Application Prototype Demo](https://drive.google.com/file/d/1Cn5siyW_3icyJRLa-k-vDn8fLrb0Dedb/view?usp=sharing)

### TechStack and Libraries Used :

* Android Studio (Java + XML)
* Material UI Components
* Mapbox SDK 
* Eazegraph
* Volley
* Glide

#### other External Libraries used :

    implementation 'androidx.multidex:multidex:2.0.1'

    implementation 'com.github.chrisbanes:PhotoView:2.0.0'

    implementation 'com.github.barteksc:android-pdf-viewer:2.8.2'

    implementation 'com.journeyapps:zxing-android-embedded:4.2.0'# ML Section of application

## ML Section 

* The repo contains two models:

### Face Recognistion

    This module is a fully functing API deployed on heroku.

    This module uses cascade detector of Open-CV to generate encodings of the face.

    The input image is converted to its encodings and are comparerd against the known encodings,
    once a sucessful match is obtained the details of the student are returned to the calling application.

#### TechStack

* Heroku 
* OpenCV 
* PIL 
* Python 
* Flask


### People Counter :

    This models uses a pretrained mobilenet to classify objects as humans and non humans,
    and gets the number of people entering the door or leaving the door.

    The model is fed by a live camrea video stream (either from a local camera or a camrea publishing
    to a specific link (IOT) )

    The live stream is breaken up into frames and the frames are used to clssify the objects as humans,
    and hence help to count the number of people inside the building.
    
#### TechStack

* OpenCV
* Cafee 
* PIL 
* Python 
* Esp32 library

# 
[Repo link for ML model](https://github.com/augsaksham/Hackfest_ML)

        
## API Documentation

[Api Link : https://facereco23.herokuapp.com/](https://facereco23.herokuapp.com/)

API Documentation: 

    Methods : 
         Type |  Function  | Usage
              |            |
         Get  | reset      |  To reset the whole database and generate new encodings
        Post  | update     |  To add encoding of new user to the database
         Post | predict    |  To recognize a face
        
    
    
