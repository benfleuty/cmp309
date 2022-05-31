<?php

include_once '/home/1900040/public_html/notsnapchat/tools/jsonHeader.php';
include_once '/home/1900040/public_html/notsnapchat/tools/enforceRequestMethod.php';
include_once '/home/1900040/public_html/notsnapchat/model/User.php';

EnforceRequestMethod('POST', 404);

$user = new User();

$user->setEmail($_POST['email']);
$user->setPassword($_POST['password']);


// successfully logged in
$result = $user->login();
if (gettype($result) != 'array')
  die(json_encode(['status' => 500]));

exit(json_encode($result));