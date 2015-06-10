# AuthMaster

This is a POC project for a Authentication Serer implmentation, loosely based on OAuth2 where the resouce server and the client server are the same server, this has been created for POC purposes and should be treated as one, you may only use this for learning purposes and never use this on any important environment. This project consists of 2 web components hosted on a tomcat8

AuthMaster
----------

This web is the auth server part, and is resposible for creating, maanging tokens to the clients
there is a property file in WEB-INF for a list of registered clients, do edit that file for ip address and port numbers used by tomcat


SampleWeb
---------

This is a sample client web that has a AuthMaster security filer to handle all the security requests and the index.jsp page is only shown upon authorisation by auth server. There is a property file in WEB-INF for auth server details and the client id, client secret to be used for communication, do edit that file for the ip address and port numbers used by tomcat
