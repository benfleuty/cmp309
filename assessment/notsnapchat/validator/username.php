<?php

function isValidUsername(string $username): array
{
  if (empty($username))
    return [
    'success' => false,
    'error' => 'empty'
    ];

  return ['success' => true];
}