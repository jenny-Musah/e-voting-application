# e-voting-application
# Registration end-point
  *for new users*

1. This end-point allows new user register on this platform.
2. This end-point sends a mail to every registered user containing the login detail of that user.
3. A code sample in Node JS using Axios is shown below.
```
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'localhost:8080/api/v1/users/register',
  'headers': {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    "emailAddress": "musahjenny99@gmail.com",
    "password": "Jennymusah#1234"
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});

```
# Response from registration request
  *This is a sample of the response from the Registration !!!*
```
{
    "loginId": 5638355948193909773,
    "statusCode": 200,
    "message": "Registration successful, check your mail for login details"
}
```

# Login end-point
  *Login in for users*
1. This end-point allows registered user login into application.
2. Users with invalid login id or password would not be logged in.
3. A code sample in Node JS using Axios is shown below

```
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'localhost:8080/api/v1/users/login',
  'headers': {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    "loginId": 5638355948193910000,
    "password": "Jennymusah#1234"
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});

```
# Response from login request
  *This is a sample of the response from the Login !!!*
```
{
    "loginId": 0,
    "statusCode": 200,
    "message": "Login successful"
}
```
# Login end-point
*Log in for admin*
1. This end-point allows admin log into the application.
2. A static login id and password has been made available for an admin.
3. Admin with invalid login id or password would not be logged in.
4. A code sample in Node JS using Axios is shown below
```
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'localhost:8080/api/v1/admin/login',
  'headers': {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    "loginId": 90078967900000,
    "password": "IamTheAdmin#123@"
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});
```
# Response from login request
*This is a sample of the response from the Login !!!*
```
{
    "voteId": 7134133076596781502,
    "message": "Admin logged in successfully"
}
```
# Login end-point
*for nominees only*
1. This end-point allows nominees log into the application.
2. Nominees with invalid login id and password would not be logged in.
3. A code sample in Node JS using Axios is shown below
```
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'localhost:8080/api/v1/nominee/login',
  'headers': {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    "loginId": "53",
    "password": "ec00729c-7NOMI#@"
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});
```
#  Response from login request
*This is a sample of the response from the Login !!!*
```
{
    "voteId": 2979259362382514928,
    "message": "Login successful"
}
```
# Declare Election end-point
*for admins only*
1. This end-point allows admin declare or create an election.
2. This end-point takes a list of nominees and adds them to the declared election.
3. Admin can not add nominees with invalid details to election.
4. Admin can not create election with invalid dates.
5. A code sample in Node JS using Axios is shown below
```
var request = require('request');
var options = {
  'method': 'POST',
  'url': 'localhost:8080/api/v1/admin/declare/election',
  'headers': {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    "electionName": "Class captain",
    "listOfNominee": [
      {
        "firstName": "Jennifer",
        "lastName": "Musah",
        "email": "jennymusah@gmail.com",
        "position": "Class captain"
      },
      {
        "firstName": "Mary",
        "lastName": "Jane",
        "email": "maryjane@gmail.com",
        "position": "Class captain"
      }
    ],
    "startAt": "02-04-2023",
    "endsAt": "03-04-2023"
  })

};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});
```
# Response from declare election request
*This is a sample of the response from declare election end-point !!!
```
{
    "electionId": 1,
    "message": "Successfully declared an election"
}
```

