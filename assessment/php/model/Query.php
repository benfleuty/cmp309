<?php
/* author Ben Fleuty */

require_once "../tools/db.php";

class Query
{
  private string $sql;
  private mysqli $connection;
  private $statement;
  private bool $executed;
  private $queryResult;

  public string $parameters;

  function __construct($sql)
  {
    $this->sql = $sql;
    $this->connection = database::connect();
    $this->statement = $this->connection->prepare($this->sql);
  }

  public function isPrepared(): bool
  {
    return gettype($this->statement) != "boolean" && gettype($this->statement) != "NULL";
  }

  public function bindParameters(&$parameterTypes, &...$params): void
  {
    call_user_func_array([$this->statement, "bind_param"], array_merge([$parameterTypes], $params));
  }

  public function execute(): void
  {
    $this->executed = $this->statement->execute();
    if ($this->executed) $this->queryResult = $this->statement->get_result();
  }

  public function successfulExecution(): bool
  {
    return $this->executed;
  }

  public function getResult()
  {
    return $this->queryResult;
  }

  public function getError()
  {
    return $this->statement->error;
  }

  public function affected_rows(): int
  {
    return $this->connection->affected_rows;
  }
}