#LumiSound

*project INF355 - Télécom Paristech (Thibault François - Jérémie Fourbil)*

##How To

In order to install and use LumiSound just tap:

	sbt run
	
First start the server application.
You can then start a number of clients of your choice.

## How LumiSound works

The server app record the sound coming from the microphone. After that according to the number of connected clients, it dispatches the analysis throught all clients.

## To Do

The initial project aimed to drive an RGB led on a raspberry pi. The remaining work is to convert the visualization to drive the light.

## Issues

We can only start remote clients under a localmachine rather than on distant clients.
There are some artefacts on the visualization.