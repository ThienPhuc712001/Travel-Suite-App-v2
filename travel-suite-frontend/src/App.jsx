import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Header from './components/Header';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import PlaceDetail from './pages/PlaceDetail';
import PlaceList from './pages/PlaceList';
import AddPlace from './pages/AddPlace';
import EditPlace from './pages/EditPlace';
import MyPlaces from './pages/MyPlaces';
import UploadImage from './pages/UploadImage';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header />
          <main>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/places" element={<PlaceList />} />
              <Route path="/places/:id" element={<PlaceDetail />} />
              <Route path="/places/new" element={<AddPlace />} />
              <Route path="/places/edit/:id" element={<EditPlace />} />
              <Route path="/my-places" element={<MyPlaces />} />
              <Route path="/places/:id/upload-image" element={<UploadImage />} />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;

