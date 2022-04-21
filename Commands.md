## Creating a new private key
```
gpg --full-gen-key
```
### List of all keys, Export 
```
gpg --list-secret-keys --keyid-format long
gpg --armor --export-secret-keys 0C5CEA1C96038404! 
```
### Publish keys
```
gpg --keyserver keyserver.ubuntu.com --send-key 0C5CEA1C96038404
```
### Change password 
```
gpg --edit-key key-id
```
### Export values to files
```
gpg --armor --export-secret-keys 0C5CEA1C96038404! > SIGN_KEY
cat > SIGN_KEY_ID
cat > SIGN_KEY_PASS
```
### export to environment variables
```
export SIGN_KEY=`cat SIGN_KEY`
export SIGN_KEY_ID=`cat SIGN_KEY_ID`
export $SIGN_KEY_PASS=`cat $SIGN_KEY_PASS`
```
### Maven sign
```
mvn sign:help -Ddetail=true -Dgoal=<goal-name>
mvn sign:sign
```

### Encoding
```
-echo 'PHNldHRpbmdzIHhtbG5zPSJodHRwOi8vbWF2ZW4uYXBhY2hlLm9yZy9TRVRUSU5HUy8xLjAuMCIKICAgICAgICAgIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiCiAgICAgICAgICB4c2k6c2NoZW1hTG9jYXRpb249Imh0dHA6Ly9tYXZlbi5hcGFjaGUub3JnL1NFVFRJTkdTLzEuMC4wCiAgICAgIGh0dHBzOi8vbWF2ZW4uYXBhY2hlLm9yZy94c2Qvc2V0dGluZ3MtMS4wLjAueHNkIj4KICAgIDxzZXJ2ZXJzPgogICAgICAgIDxzZXJ2ZXI+CiAgICAgICAgICAgIDxpZD54eXotcmVsZWFzZXM8L2lkPgogICAgICAgICAgICA8dXNlcm5hbWU+JHtteS5yZXBvLnVzZXJ9PC91c2VybmFtZT4KICAgICAgICAgICAgPHBhc3N3b3JkPiR7bXkucmVwby5wYXNzd29yZH08L3Bhc3N3b3JkPgogICAgICAgICAgICA8IS0tIGRvIHlvdSByZWFsbHkgdGhpbmsgSSB3b3VsZCBoYXZlIHB1dCB0aGUgcGFzc293cmQgaGVyZT8gLS0+CiAgICAgICAgPC9zZXJ2ZXI+CiAgICAgICAgPHNlcnZlcj4KICAgICAgICAgICAgPGlkPnh5ei1zbmFwc2hvdHM8L2lkPgogICAgICAgICAgICAgPHVzZXJuYW1lPiR7bXkucmVwby51c2VyfTwvdXNlcm5hbWU+CiAgICAgICAgICAgIDxwYXNzd29yZD4ke215LnJlcG8ucGFzc3dvcmR9PC9wYXNzd29yZD4KICAgICAgICA8L3NlcnZlcj4KICAgIDwvc2VydmVycz4KPC9zZXR0aW5ncw==' | base64 - decode > settings.xml


```

### Update project version
```
mvn release:update-versions
mvn versions:set -DnewVersion=1.3.0
```