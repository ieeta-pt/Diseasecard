Diseasecard
===========

// TODO: Contextualization





<br><br>How to deploy Diseasecard?
==========================

## First steps

1.	Please create your `.env` file here, using `.env.example` as reference.
For local installation, you can just copy the `.env.example` content to a new file. 

2.  The system runs at 'SERVERNAME:REVERSEPROXY_PORT/diseasecard'. If you need to change the domain you can edit the '.env' file variable 'SERVERNAME'. If you need to change the used port you can edit the '.env' file variable 'REVERSEPROXY_PORT'. If you chose to not edit the '.env' file, the system will run at 'http://localhost:80/diseasecard'.

3.  After all changes are complete, the system needs to be built. From your project directory, build the system by running 'docker-compose build'. This step can take a couple of minutes until the image is built. It might take some time if it is the first time you are building the images. If it is not, it will use as much cached information as possible and the building process will be faster. When it is finished building, you will be notified in the terminal with a "Build Complete" message.

4.  The image is built and now it is time to run the application. For that, simply run the command 'docker-compose up --remove-orphans'. This process will take some time until the application is up and running. You can check the state of the application by reading the container logs: 
        
        $ docker logs  dc_backend

    If the container's name is not dc_backend, please check its name using the following command:
        
        $ docker container ls

<br>
*Note*: if you are running the application with the default ports, you should be able to access it at http://localhost:80/diseasecard
<br><br><br>


## Environment variables

// TODO: Variables Description

<br><br>
How to build Diseasecard system?
================================

As previously mentioned, Diseasecard system is internally organized according to an ontology that allows to define Entities, Concepts, Resources and Parsers. This instances allows Diseasecard to retrieve information from different sources in different formats. This process is what allows Diseasecard to present all the data to the end user in an organized and efficient way. 

*Note*: To get more information about the processo of addind data to Diseasecard you can check this file. 

<br>The process of adding data to Diseasecard system can be performed in Diseacard Admin subplatform. In Diseasecard Admin you can submit an ontology following the Diseasecard Ontology format or add instances individually. This system setup process can be perform in Diseasecard Admin > Source Management > Management > Add Instances menu. 

*Note*: To analyse a valid ontology file example you can check this file. 

<br>After the process of adding instances to Diseasecard system is concluded, you can check the current Ontology Structure on the same page, inside 'Ontology Structure' card. Inside this card you can not only confirm the ontology submited but also edit or remove any instance from it. 

*Note*: The process of editing or removing instances can not be perform while the system is building or unbuilding. 

<br>After confirming the ontology structure, you may want to build the system. To do this, simply click on the 'Build' button on the 'System Status' card. The system build process includes: reading resources, caching information, and indexing all data. Given the complexity of this process, it may take some time to complete. When it is completed, the system will automatically update its status. 

If you have entered a correct and valid ontology, after completing this process you can go to the Diseasecard platform and browse the existing diseases. 

<br>
<br>
// TODO: Add links to files and add more information.. 