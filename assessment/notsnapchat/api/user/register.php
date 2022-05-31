<?php

include_once '/home/1900040/public_html/notsnapchat/tools/jsonHeader.php';
include_once '/home/1900040/public_html/notsnapchat/tools/enforceRequestMethod.php';
include_once '/home/1900040/public_html/notsnapchat/model/User.php';

EnforceRequestMethod('POST', 404);

$user = new User();

$user->setEmail($_POST['email']);
$user->setUsername($_POST['username']);
$user->setPassword($_POST['password']);
$user->setConfirmPassword($_POST['confirmPassword']);

$result = $user->register();

if (gettype($result) != 'array')
  die(json_encode(['status' => 500]));


if ($result['status'] != 200) {
  die(__LINE__);
  // todo handle error
}

if ($result['response']['status'] != 200) {
  // did not register
  // todo handle not registered
  exit();
}

// successfully logged in
$result = $user->login();
if (gettype($result) != 'array')
  die(json_encode(['status' => 500]));

exit(json_encode($result));