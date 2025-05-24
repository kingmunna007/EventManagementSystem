# EventEase React Frontend

This project is the React-based frontend for the EventEase event management system, created using Vite for fast development and build times.

## Getting Started

1. Install dependencies:
   ```sh
   npm install
   ```
2. Start the development server:
   ```sh
   npm run dev
   ```
3. The app will run on [http://localhost:5173](http://localhost:5173) by default.

## Project Structure
- `src/` - React source code (components, pages, etc.)
- `public/` - Static assets

## Migration
This project is intended for migrating the existing static HTML/CSS/JS frontend to React components and pages. The original static frontend remains untouched in the `frontend` folder.

## API Integration
- Use `fetch` or `axios` to connect to the backend API (Spring Boot).

## Deployment
- After building (`npm run build`), deploy the `dist/` folder to your preferred static hosting provider.

---
