package userservice.authz

default allow = false

allow {
  input.method = "GET"
  input.path = ["users"]
  allowed[user]
}

allowed[user] {
  user = data.users[_]
  user.username = input.subject.user
  user.organization = input.subject.attributes.organization
}

allowed[user] {
  user = data.users[_]
  user.manager = input.subject.user
  user.organization = input.subject.attributes.organization
}