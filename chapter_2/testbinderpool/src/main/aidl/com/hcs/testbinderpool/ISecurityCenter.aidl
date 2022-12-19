package com.hcs.testbinderpool;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}