<?php

function isValidPassword(string $password): array
{
  if (empty($password))
    return [
    'success' => false,
    'error' => 'empty'
    ];

  return ['success' => true];
}

function areValidPasswords(string $password, string $confirm): array
{
  if (empty($password))
    return [
    'success' => false,
    'error' => 'empty'
    ];

  if ($password !== $confirm)
    return ['success' => false, 'error' => 'no match'];

  return ['success' => true];
}
