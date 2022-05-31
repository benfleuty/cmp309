<?php

function EnforceRequestMethod(string $requestMethod, int $httpErrorCode){
  if ($_SERVER['REQUEST_METHOD'] != $requestMethod) {
    exit(json_encode(['status' => $httpErrorCode]));
  }
}