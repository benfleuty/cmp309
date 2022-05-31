<?php

include_once '/home/1900040/public_html/notsnapchat/model/Query.php';
include_once '/home/1900040/public_html/notsnapchat/validator/email.php';
include_once '/home/1900040/public_html/notsnapchat/validator/password.php';

class User
{
  private int $id;
  private string $email;
  private string $username;
  private string $password;
  private string $confirmPassword;


  /**
   * @return array|void
   */
  public function login()
  {
    $result = isValidEmail($this->email);
    $success = $result['success'];
    if (!$success)
      $output['errors'][] = $result['error'];

    $result = isValidPassword($this->password);

    if (!$result['success'])
      $output['errors'][] = $result['error'];


    // if success is true then no errors were detected
    // otherwise there was at least one error

    if (!$success) {
      // error(s) occurred
      unset($output['success']);
      $output['status'] = 400;
      exit(json_encode($output));
    }

    /////////////////////////////////
    /// Connect to database
    /// and verify user
    /////////////////////////////////

    $sql = "SELECT id,password FROM notsnapchat_users WHERE email = ?";
    $query = new Query($sql);

    if (!$query->isPrepared()) {
      // todo handle failure
      die(strval(__LINE__));
    }

    $paramsTypes = "s";
    $query->bindParameters($paramsTypes, $this->email);

    $query->execute();

    if (!$query->successfulExecution()) {
      // todo handle error
      die(strval(__LINE__));
    }

    $result = $query->getResult();

    switch ($result->num_rows) {
      case 0:
        // email/password combo not found
        $output['status'] = 200;
        $output['response']['status'] = 404;
        $output = json_encode($output);
        exit($output);
      case 1:
        // a user has been found
        $row = $result->fetch_assoc();
        break;
      default:
        // todo error
        die(strval(__LINE__));
    }

    // row should be an array otherwise error
    if (gettype($row) != "array") {
      // todo handle error
      die(strval(__LINE__));
    }

    $passwordsMatch = password_verify($this->password, $row['password']);

    // if passwords do not match
    if (!$passwordsMatch) {
      // email/password combo not found
      $output['status'] = 200;
      $output['response']['status'] = 400;
      $output = json_encode($output);
      exit($output);
    }

    // verified login details

    $output['status'] = 200;
    $output['response']['status'] = 200;

    // get user information by id

    $sql = "SELECT id, email, username FROM notsnapchat_users WHERE id = ?";
    $query = new Query($sql);

    if (!$query->isPrepared()) {
      // todo handle failure
      die(strval(__LINE__));
    }

    $paramsTypes = "i";

    $query->bindParameters($paramsTypes, $row["id"]);

    $query->execute();

    if (!$query->successfulExecution()) {
      // todo handle error
      die(strval(__LINE__));
    }

    $result = $query->getResult();

    switch ($result->num_rows) {
      case 0:
        // email/password combo not found
        $output['status'] = 200;
        $output['response']['status'] = 404;
        $output = json_encode($output);
        exit($output);
      case 1:
        // a user has been found
        $row = $result->fetch_assoc();
        break;
      default:
        // todo error
        die(strval(__LINE__));
    }

    // row should be an array otherwise error
    if (gettype($row) != "array") {
      // todo handle error
      die(strval(__LINE__));
    }

    $user['id'] = $row['id'];
    $user['email'] = $row['email'];
    $user['username'] = $row['username'];

    $output['response']['user'] = $user;

    return $output;
  }

  /**
   * @return array|void
   */
  public function register()
  {
    $result = isValidEmail($_POST['email']);
    $success = $result['success'];
    if (!$success)
      $output['errors'][] = $result['error'];

    $result = areValidPasswords($_POST['password'], $_POST['confirmPassword']);

    if (!$result['success'])
      $output['errors'][] = $result['error'];

    $result = isValidUsername($_POST['username']);

    if ($result['success'])
      $output['errors'][] = $result['error'];

    // if success is true then no errors were detected
    // otherwise there was at least one error

    if (!$success) {
      // error(s) occurred
      unset($output['success']);
      $output['status'] = 400;
      exit(json_encode($output));
    }

    /////////////////////////////////
    /// Connect to database
    /// and try register user
    /////////////////////////////////

    // check if user already exists


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

      return $output;
    }

    $output['status'] = 200;
    $output['response']['status'] = 200;

    return $output;


  }

  public function update(User $updatedUser)
  {
    if ($this->password !== $this->confirmPassword)
      return ['status' => 200, 'error' => 'Passwords do not match'];

    // logged in and password confimed
    // can update

    $c = 0;

    if ($this->email !== $updatedUser->email)
      $c += 1;

    if ($this->username !== $updatedUser->username)
      $c += 2;


    $emailToUpdate = false;
    $usernameToUpdate = false;
    $paramTypes = "";


    switch ($c) {
      // no changes
      case 0:
        $output['status'] = 200;
        break;

      // email changed
      case 1:
        $val = 'email = ?';
        $paramTypes = 's';
        $usernameToUpdate = false;
        $emailToUpdate = true;
        break;

      // username changed
      case 2:
        $val = 'username = ?';
        $paramTypes = 's';
        $emailToUpdate = false;
        $usernameToUpdate = true;
        break;

      // email and username changed
      case 3:
        $val = 'email = ?, username = ?';
        $emailToUpdate = $username = true;
        $paramTypes = 'ss';
        break;

      // error
      default:
        // todo error
        die(json_encode(['status' => 500]));
    }


    $sql = "UPDATE notsnapchat_users SET " . $val . " WHERE id = ?";

    $paramTypes .= 's';

    $query = new Query($sql);

    // update both
    if ($emailToUpdate && $usernameToUpdate)
      $query->bindParameters($paramsTypes, $emailToUpdate, $usernameToUpdate);

    else if ($emailToUpdate)
      $query->bindParameters($paramTypes, $this->email);

    else if ($usernameToUpdate)
      $query->bindParameters($paramTypes, $this->username);

    // else // todo handle error


    $query->execute();

    if (!$query->successfulExecution()) {
      // todo handle error
      die(strval(__LINE__));
    }

    $result = $query->getResult();

    if ($result->num_rows != 1) {
      // todo handle register error
      $output['status'] = 200;
      $output['response']['status'] = 500;

      return $output;
    }

    $output['status'] = 200;
    $output['response']['status'] = 200;

    return $output;
  }

  /**
   * @return string
   */
  public
  function getEmail(): string
  {
    return $this->email;
  }

  /**
   * @param string $email
   */
  public
  function setEmail(string $email): void
  {
    $this->email = $email;
  }

  /**
   * @return string
   */
  public
  function getUsername(): string
  {
    return $this->username;
  }

  /**
   * @param string $username
   */
  public
  function setUsername(string $username): void
  {
    $this->username = $username;
  }

  /**
   * @return string
   */
  public
  function getPassword(): string
  {
    return $this->password;
  }

  /**
   * @param string $password
   */
  public
  function setPassword(string $password): void
  {
    $this->password = $password;
  }

  /**
   * @return string
   */
  public
  function getConfirmPassword(): string
  {
    return $this->confirmPassword;
  }

  /**
   * @param string $confirmPassword
   */
  public
  function setConfirmPassword(string $confirmPassword): void
  {
    $this->confirmPassword = $confirmPassword;
  }

  /**
   * @return int
   */
  public
  function getId(): int
  {
    return $this->id;
  }

  /**
   * @param int $id
   */
  public
  function setId(int $id): void
  {
    $this->id = $id;
  }
}