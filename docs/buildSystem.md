# Configuring the Build System

To improve the build process, it's recommended to create a `local.json` file in the `settings` directory. This file allows you to customize various aspects of the build system. Here's an example configuration:
```json
{
  "copy": [
    "path/to/directory"
  ], 
  "build": {
    "buildAndroid": false
  }, 
  "dbt": {
    "run": [
      "run command"
    ], 
    "frun": [
      "first run command"
    ]
  }
}
```


## copy field
This section specifies the paths where the compiled mod will be copied when running `copyBuildRelease` or `runBuildRelease`. These commands ensure that the mod is automatically deployed to the appropriate location after the build process.

## build.buildAndroid field
This flag determines whether the mod should be compiled for the Android platform. Set it to true to enable Android build, or false to skip it.

## dbt.run field
This section defines the commands that will be executed when `run` or `runBuildRelease` is invoked. These commands typically perform the same tasks as copyBuildRelease, but also launch the application after the build and copy operations are completed.

## dbt.frun field
This section specifies the commands that will be executed when `firstRun` is launched. These commands are typically used for initial setup or configuration tasks.

## Example configuration:
```json
{
  "copy": [
    "/home/durmiendo/.local/share/Mindustry/mods"
  ], 
  "build": {
    "buildAndroid": false
  }, 
  "dbt": {
    "run": [
      "java -jar /home/durmiendo/mindustry/Mindustry.jar"
    ],
    "frun": [
      "nautilus /home/durmiendo/.local/share/Mindustry"
    ]
  }
}
```

In this example, the compiled mod will be copied to the `/home/durmiendo/.local/share/Mindustry/mods` directory. The build process will not include an Android build, and the `run` command will launch the Mindustry application, while the `frun` command will open the Mindustry directory in the file manager.
