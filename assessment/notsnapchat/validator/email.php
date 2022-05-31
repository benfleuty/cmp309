<?php

function isValidEmail(string $email): array
{
  if (empty($email))
    return [
    'success' => false,
    'error' => 'empty'
    ];

  if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    return [
    'success' => false,
    'error' => 'invalid'
    ];
  }

  return ['success' => true];
}