# Social Photo Network - Server
An example of a backend for the Social Photo Network written to illustrate the Speedment ORM. Below is the Http API used by the client to communicate with the server.

## Register account
**Target:** http://[host]/register

**Params:** mail, password

**Return:** *sessionkey* if successful, else *false*.

## Login to service
**Target:** http://[host]/login

**Params:** mail, password

**Return:** *sessionkey* if successful, else *false*.

## Upload image
**Target:** http://[host]/upload

**Params:** title, description, imgdata, sessionkey

**Return:** *true* if successful, else *false*.

## Find self
**Target:** http://[host]/self

**Params:** *sessionkey*

**Return:** The following json object, else *false*:
```javascript
{
    "id" : <long>,
    "mail" : "<string>",
    "firstName" : "<string>",
    "lastName" : "<string>",
    "avatar" : "<base64>"
}
```

## Find other users
**Target:** http://[host]/find

**Params:** freetext, sessionkey

**Return:** The following json object, else *false*:
```javascript
{
    "users" : [
        {
            "id" : <long>,
            "mail" : "<string>",
            "firstName" : "<string>",
            "lastName" : "<string>",
            "avatar" : "<base64>"
        }, (...)
    ]
}
```

## Follow user
**Target:** http://[host]/follow

**Params:** userid, sessionkey

**Return:** *true* if successful, else *false*.

## Browse images
**Target:** http://[host]/browse

**Params:** sessionkey, [from | to]

**Return:** The following json object or *false*:
```javascript
{
    "images" : [
        {
            "id" : <long>,
            "title" : "<string>",
            "description" : "<string>",
            "imgdata" : "<base64>"
            "uploaded" : "<localdatetime>",
            "uploader" : {
                "id" : <long>,
                "mail" : "<string>",
                "firstName" : "<string>",
                "lastName" : "<string>",
                "avatar" : "<base64>"
            }
        }, (...)
    ]
}
```

## Update profile
**Target:** http://[host]/update

**Params:** mail, firstname, lastname, imgdata, sessionkey

**Return:** The following json object, else *false*:
```javascript
{
    "id" : <long>,
    "mail" : "<string>",
    "firstName" : "<string>",
    "lastName" : "<string>",
    "avatar" : "<base64>"
}
```
