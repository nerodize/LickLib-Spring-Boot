import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import { Layout } from './components/layout/Layout';
import { HomePage } from './components/pages/HomePage';
import { TracksPage } from './components/pages/TracksPage';
import { UserList } from './components/pages/UsersPage';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Layout ist ein "Wrapper" für alle internen Seiten */}
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage onStart={function (): void {
            throw new Error('Function not implemented.');
          } } />} /> 
          <Route path="tracks" element={<TracksPage />} />
          <Route path="users" element={<UserList />} />
          
          {/* Catch-all: Wenn Route nicht existiert, zurück nach Home */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}