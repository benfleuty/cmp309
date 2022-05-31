<?php
ini_set("display_errors", 1);
error_reporting(E_ALL);

include_once '/home/1900040/public_html/notsnapchat/tools/jsonHeader.php';
include_once '/home/1900040/public_html/notsnapchat/tools/enforceRequestMethod.php';
include_once '/home/1900040/public_html/notsnapchat/model/User.php';

EnforceRequestMethod('POST', 404);

$currentUser = new User();
$updatedUser = new User();

$currentUser->setEmail($_POST['email']);
$currentUser->setUsername($_POST['username']);
$currentUser->setPassword($_POST['password']);
$currentUser->setconfirmPassword($_POST['confirmPassword']);

if (isset($_POST['newEmail']))
  $updatedUser->setEmail($_POST['newEmail']);
else $updatedUser->setEmail($currentUser->getEmail());
if (isset($_POST['newUsername']))
  $updatedUser->setUsername($_POST['newUsername']);
else $updatedUser->setUsername($currentUser->getUsername());


// successfully logged in
$result = $currentUser->login();
if (gettype($result) != 'array')
  die(json_encode(['status' => 500]));

$currentUser->setId($result['user']['id']);
$result = $currentUser->update($updatedUser);

if ($result['status'] !== 200) // todo handle error
{
}

// success

$output['status'] = 200;
$output['user']['id'] = $currentUser->getId();
if ($currentUser->getEmail() !== $updatedUser->getEmail())
  $currentUser->setEmail($updatedUser->getEmail());

if ($currentUser->getUsername() !== $updatedUser->getUsername())
  $currentUser->setUsername($updatedUser->getUsername());

$output['user']['email'] = $currentUser->getEmail();
$output['user']['username'] = $currentUser->getUsername();

exit(json_encode($output));