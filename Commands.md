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