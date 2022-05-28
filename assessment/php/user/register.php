<?php
/* author Ben Fleuty */
ini_set("display_errors", 1);
error_reporting(E_ALL);

include_once '../tools/json-header.php';
include_once '../model/Query.php';

// must be post
if ($_SERVER['REQUEST_METHOD'] != 'POST') {
  exit(json_encode(['status' => 404]));
}

$output = ['status' => 0];

// check for email
if (empty($_POST['email'])) {
  $output['status'] = 400;
  $output['email']['error'] = 'empty';
} // validate email
else {
  $email = $_POST["email"];

  // email is not valid
  if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $output['status'] = 400;
    $output['email']['error'] = 'invalid';
  }
}

// check for password
if (empty($_POST['password'])) {
  $output['status'] = 400;
  $output['password']['error'] = 'empty';
} // validate password
else {
  $unsafe_password = $_POST["password"];
  // password validation can be done here

  $safe_password = password_hash($unsafe_password, PASSWORD_DEFAULT);
  $unsafe_password = NULL;
}

// check for username
if (empty($_POST['username'])) {
  $output['status'] = 400;
  $output['username']['error'] = 'empty';
} // validate username
else {
  $username = $_POST["username"];
  // username validation can be done here

}

// if output['status'] is 0 then no errors were detected
// otherwise there was at least one error

if ($output['status'] !== 0) {
  // error(s) occurred
  exit(json_encode($output));
}

/////////////////////////////////
/// Connect to database
/// and try register user
/////////////////////////////////


// check if user already exists

$sql = "SELECT id FROM notsnapchat_users WHERE email = ? OR username = ?";
$query = new Query($sql);

if (!$query->isPrepared()) {
  // todo handle failure
  die(strval(__LINE__));
}

$paramsTypes = "ss";

$query->bindParameters($paramsTypes, $email, $username);

$query->execute();

if (!$query->successfulExecution()) {
  // todo handle error
  die(strval(__LINE__));
}

$result = $query->getResult();

// user found
// cannot register user
if ($result->num_rows !== 0) {
  $output['status'] = 200;
  $output['response']['status'] = 400;
  $output = json_encode($output);
  exit($output);
}

// register user
$sql = "INSERT INTO notsnapchat_users (email,password,username) VALUES (?,?,?)";
$query = new Query($sql);

if (!$query->isPrepared()) {
  // todo handle failure
  die(strval(__LINE__));
}

$paramsTypes = "sss";

$query->bindParameters($paramsTypes, $email, $safe_password, $username);

$query->execute();

if (!$query->successfulExecution()) {
  // todo handle error
  die(strval(__LINE__));
}

$result = $query->getResult();

if ($result->num_rows != 0) {
  // todo handle register error
  $output['status'] = 200;
  $output['response']['status'] = 500;

  $output = json_encode($output);
  exit($output);
}

$output['status'] = 200;
$output['response']['status'] = 200;

$output = json_encode($output);
exit($output);