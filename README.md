# RoleplayBot - Discord RPs the better way

Hello and welcome. In the following document we will guide you through the features and configuration of our discord bot.

## Features
For now our features are:
* A customisable game cycle
* Messages send with your pattern
* Custom bot appearance

But we intend to add the following:
* Food system
* Welcome messages
* Player specific messages

Feel free to request features witch are not in this list. We will be pleased by your help.

## Configuration
The configuration files are in json format. You will understand it quite fast by looking in the [ExampleConfig](https://github.com/NikBenson/RoleplayBot/ExampleConfig) directory.
You will find the documentation of the values in the [Wiki](https://github.com/NikBenson/RoleplayBot/wiki).

One special thing you need to do is registering the bot at the [Developer Portal](https://discord.com/developers/).
Please generate a token for your bot and add it to your botinfo.json.

The last step to go is to start the bot. In order to do, so you have to run the RoleplayBot.jar from the wanted release and execute it with the following command:
```shell
java -jar /path/to/RoleplayBot.jar /path/of/your/configuration/directory
```
Please make sure to use absolute not relative paths.