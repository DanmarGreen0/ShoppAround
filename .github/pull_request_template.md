## Risk and Impact Analysis
Users need a way of securing their accounts from other users. Without a security functionality, different users may potentially steal other user accounts or information. Therefore, security is a vital feature of any computer application, especially web applications that store important or confidential data. Also, know would ever want to use an application that exposes their information to the entire world.

## Overview
I have implemented a security feature that allows users to log in to their accounts. I have also implemented web service endpoint security that enforces user access. However, once a user logs into their account using a web browser, they can't log out of their account unless they end the browser session. In a use case where multiple users interact with a single device to sign in to their accounts, this security flaw would leave any user at risk of another user using their account. 

## New feature/API (non-breaking change which adds functionality)
- security
  - user login
  - admin user can view all user accounts and their information
  - basic user can only view their account information


## Reviewer Notes
Checklist:

## Release Agreement

