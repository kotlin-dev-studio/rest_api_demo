rootProject.name = "rest_api_demo"

include("common")
include("user")
include("product")
include("payment")

project(":common").projectDir = "user" as File
project(":user").projectDir = "user" as File
project(":product").projectDir = "product" as File
project(":payment").projectDir = "payment" as File
