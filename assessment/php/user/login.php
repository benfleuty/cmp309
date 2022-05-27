<?php
/* author Ben Fleuty */
include_once '../tools/json-header.php';

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
  //$password = $_POST["password"];
  // password validation can be done here
}

// if output['status'] is 0 then no errors were detected
// otherwise there was at least one error

if ($output['status'] !== 0) {
  // error(s) occurred
  exit(json_encode($output));
}

