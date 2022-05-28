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

// if output['status'] is 0 then no errors were detected
// otherwise there was at least one error

if ($output['status'] !== 0) {
  // error(s) occurred
  exit(json_encode($output));
}

/////////////////////////////////
/// Connect to database
/// and verify user
/////////////////////////////////

$sql = "SELECT id FROM notsnapchat_users WHERE email = ? AND password = ?";
$query = new Query($sql);

if (!$query->isPrepared()) {
  // todo handle failure
  die(strval(__LINE__));
}

$paramsTypes = "ss";

$query->bindParameters($paramsTypes, $email, $safe_password);

$query->execute();

if(!$query->successfulExecution()){
  // todo handle error
  die(strval(__LINE__));
}

$result = $query->getResult();

switch ($result->num_rows){
  case 0:
    // todo no user found
    $output['status'] = 200;
    $output['response']['status'] = 404;
    $output = json_encode($output);
    exit($output);
  case 1:
    // todo user found
    $row = $result->fetch_assoc();
    break;
  default:
    // todo error
    die(strval(__LINE__));
}

// row should be an array otherwise error
if(gettype($row) != "array"){
  // todo handle error
  die(strval(__LINE__));
}

$output['status'] = 200;
$output['response']['status'] = 200;
$output['response']['data']['id'] = $row['id'];

$output = json_encode($output);
exit($output);