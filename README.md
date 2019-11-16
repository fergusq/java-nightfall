java-nightfall
==============

A clone of *SPYBOT: The nightfall incident* in Java.
Originally forked from https://code.google.com/p/java-nightfall/
(now available at https://github.com/stravant/java-nightfall)

This forks improves the game by adding most of the features found in the original Spybot game,
including shops, messages, healing and grid modifying (BitMan attack).
The zones are the most important lacking feature.

## Resource Packs

Unzip these in `~/.nightfall/AddOns/`.

* [NightfallPack](https://www.kaivos.org/pelit/NightfallPack.zip): Contains textures from the original _The Nightfall Incident_.
* [DevPack](https://www.kaivos.org/pelit/DevPack.zip): Contains tutorial and remakes of levels from the original game.
* [HackerBases](https://www.kaivos.org/pelit/HackerBases.zip): A small story of a hacker whose employer's servers are corrupted.

```sh
mkdir -p ~/.nightfall/AddOns
cd ~/.nightfall/AddOns
wget https://www.kaivos.org/pelit/{NightfallPack,DevPack,HackerBases}.zip
unzip NightfallPack.zip
unzip DevPack.zip
unzip HackerBases.zip
```

The game will not work without NightfallPack and DevPack! If you launched the game before installing them, remove `~/.nightfall/SaveData.json` and install the packs.

## Compiling

There is a prebuilt version: [nightfall.jar](https://github.com/fergusq/java-nightfall/releases/download/v1/nightfall.jar).

```
make nightfall.jar
```

## Running

```
java -cp nightfall.jar game.WMain
```
