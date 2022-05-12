# Shop Server 

## Physical Emulator

1. Connect your emulator to Android Studio
2. List all your devices with this command

```
adb devices
```
You will see your device on the terminal
3. Use this command in order to allow your phone to access the server
```
adb -s <device> reverse tcp:3000 tcp:3000
```

## Run

```
docker-compose up 
```