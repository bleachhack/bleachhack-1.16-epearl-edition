
# BleachHack VpEdition
![](https://img.shields.io/tokei/lines/github/BleachDrinker420/bleachhack-1.14?style=flat-square)
![](https://img.shields.io/github/languages/code-size/HerraVp/bleachhack-VpEdition?style=flat-square)
![](https://img.shields.io/github/last-commit/HerraVp/bleachhack-VpEdition?style=flat-square)

My own skid of BleachHack

Works on fabric 1.16 


## Real BleachHack
> Website: https://bleachhack.github.io/  
> Discord: https://discord.gg/b5Wc4nQ

## Installation
### For normal people

Download [fabric for minecraft 1.16/1.17](https://fabricmc.net/use/).
Download the latest version of BleachHack for your Minecraft version [from the releases section](https://github.com/BleachDrinker420/BleachHack/releases).


On Windows: Type %appdata% into the Windows search bar; open the folder that comes up and then open '.minecraft'.

On Mac: Click on the desktop, press Command+Shift+G, type ~/Library and press enter; then open 'Application Support' and finally '.minecraft'.

On Linux: in a terminal window, type 'xdg-open ~/.minecraft'... or if you like working at the commandline, 'cd ~/.minecraft'.

Then inside that folder, you should see a folder named 'mods'. (If you don't see one, make sure you've installed Fabric already and started Minecraft again once).
That's where you'll put any mods you want to install.

### For (200 IQ) developers

Download the project.
Start A Command Prompt/Terminal in the BleachHack-Fabric-(*Version*) folder.
Generate the needed files for your preferred IDE.

***Eclipse***

  On Windows:
  > gradlew genSources eclipse
  
  On Linux:
  > chmod +x ./gradlew  
  >./gradlew genSources eclipse

  Start a new workspace in eclipse.
  Click File > Import... > Gradle > Gradle Project.
  Select the BleachHack-Fabric-(*Version*) folder.

***Other IDE's***

  Use [this link](https://fabricmc.net/wiki/tutorial:setup) for more information.
  It should be pretty similar to the eclipse setup.

## Recommended Mods

Here are some nice to have mods that are compatible with BleachHack, none of these require Fabric API.

### [Multiconnect](https://github.com/Earthcomputer/multiconnect)
Multiconnect allows you to connect to any 1.9-1.16 server from a 1.16 client.

### [Baritone](https://github.com/cabaletta/baritone)
Baritone allows you to automate tasks such as walking, mining or building.

## License

If you are distributing a custom version of BleachHack or a mod with ported features of BleachHack, you are **required** to disclose the source code, state changes, use a compatible license, and follow the [licence terms](https://github.com/BleachDrinker420/bleachhack-1.14/blob/master/LICENSE).
