# Briefly about the build system
For a better build, it is recommended to create a local.json file in settings
example:
```json
{
  "copy": [
    "your path from dir"
  ],
  "build": {
    "buildAndroid": false
  },
  "dbt": {
    "run": [
      "command №1"
    ],
    "frun": [
      "command №2"
    ]
  }
}
```

## field `copy`
the paths where the compiled mod will be copied when running `copyBuildRelease` or `runCopyBuildRelease` are indicated


## field `build.buildAndroid`
indicates whether the mod should be compiled for Android.


## field `drm-build-tools.run`
Specify what will be executed when `run` or `runBuildRelease` is run (does everything the same as `copyBuildRelease`, but runs the launch after `run`)


## field `drm-build-tools.frun`
commands that are executed when `firstRun` is launched