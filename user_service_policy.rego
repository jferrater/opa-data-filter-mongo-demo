package userservice.authz

default allow = false

allow {
  input.method = "GET"
  input.path = ["users"]
  allowed[user]
}

allowed[user] {
  user = data.users[_]
  user.username = input.subject.username
}

allowed[user] {
  user = data.users[_]
  user.organization = input.subject.attributes.organization
}