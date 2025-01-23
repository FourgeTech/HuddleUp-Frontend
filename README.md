# HuddleUp - Sports Team Management App

## Group Members:
- Yugen Naidoo  
- Aidan Lottering  
- Blake Marais  
- Mark Koshiaris  

## Links:
- [HuddleUp Frontend (Android Studio App Repo)](https://github.com/FourgeTech/HuddleUp-Frontend)  
- [HuddleUp Backend (Backend API Repo)](https://github.com/FourgeTech/HuddleUp-Backend)  

## Overview
HuddleUp is a sports team management and communication app designed to help coaches and team managers organize schedules, track attendance, share updates, and facilitate communication among team members. 

This repository contains the Android Studio project for the initial phase of HuddleUp, which integrates Firebase for authentication and backend services and uses Firestore as the database.

## Features (Phase 1)

### User Registration & Login (SSO)
- Users can register and log in to the app using Single Sign-On (SSO) via Google or Facebook.
- Authentication is handled by Firebase Auth.

### User Settings
- Users can update their profile information and change their settings, including notification preferences and profile details.

### Team Creation & Join
- Managers can create new teams with a custom team code.
- Team members can join existing teams by entering the custom team code.

### Chat Feature
- Real-time chat functionality for team communication, using Firestore to sync messages between team members.
- Separate chat rooms for managers and teams.

### Announcements (Locker Room)
- Managers can send team-wide announcements (broadcasts) that all team members can view.

### REST API Integration
- The app connects to a custom-built REST API that handles interactions with the database (e.g., creating teams, retrieving user information).
- The API is hosted and connected to Firebase and Firestore for storing and managing team and user data.

### Events
- Managers can schedule events and practices.
- Players can view these events and see the related details.

### Biometric Authentication
- Users can log in using biometrics.
- Manual login is required first before biometric authentication can be assigned.

### Multi-Language Application
- Users can select their preferred language.
- Available languages: **English**, **Afrikaans**, and **Japanese**.

### Notifications
- Notifications are sent for announcements.
- Players can toggle notifications on or off in their settings.

### Team View
- Managers can select a lineup for the squad in the team view.
- Players can view the selected squad to know who will be playing in upcoming matches.

## Project Structure
The project is developed in Android Studio and utilizes the following technologies:
- **Firebase Functions**: For backend logic.
- **Firebase Auth**: For handling user authentication and SSO.
- **Firestore**: Cloud database used for storing team, user, and chat data.

## Usage
### Register/Login:
- Users can register or log in via Google or Facebook using Firebase SSO.

### Create or Join a Team:
- Managers can create teams by generating a custom code.
- Team members can join a team by entering the provided code.

### Chat:
- Managers and team members can communicate via real-time chat.
- Separate chat rooms for managers and teams.

### Announcements:
- Managers can post announcements that are visible to all team members.

### Events:
- Managers can create events that are visible to all team members.

### Biometric Authentication:
- Users can log in using biometric authentication if they have signed in before.

### Multi-Language:
- Users can select their preferred language in the settings. Options include **English**, **Afrikaans**, and **Japanese**.

### Notifications:
- Once announcements are posted by managers, all team members will receive a notification regarding that announcement.

### Team View:
- Managers can select team members by tapping on the desired positions. A list of all unselected players will be shown. Once the manager selects a player for the position, the lineup will be visible to all team members.
