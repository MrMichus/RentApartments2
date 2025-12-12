import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar/Navbar';
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute';

// Pages
import Home from './pages/Home/Home';
import Login from './pages/Auth/Login';
import Register from './pages/Auth/Register';
import MieszkaniaList from './pages/Mieszkania/MieszkaniaList';
import MieszkanieDetail from './pages/Mieszkania/MieszkanieDetail';
import AddMieszkanie from './pages/Mieszkania/AddMieszkanie';
import MyMieszkania from './pages/Mieszkania/MyMieszkania';
import Chat from './pages/Chat/Chat';
import Profile from './pages/Profile/Profile';
import AdminPanel from './pages/Admin/AdminPanel';

import './App.css';

function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="app">
                    <Navbar />
                    <main className="main-content">
                        <Routes>
                            {/* Public Routes */}
                            <Route path="/" element={<Home />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/mieszkania" element={<MieszkaniaList />} />
                            <Route path="/mieszkania/:id" element={<MieszkanieDetail />} />

                            {/* Protected Routes - User */}
                            <Route path="/dodaj-mieszkanie" element={
                                <ProtectedRoute>
                                    <AddMieszkanie />
                                </ProtectedRoute>
                            } />
                            <Route path="/moje-mieszkania" element={
                                <ProtectedRoute>
                                    <MyMieszkania />
                                </ProtectedRoute>
                            } />
                            <Route path="/chat" element={
                                <ProtectedRoute>
                                    <Chat />
                                </ProtectedRoute>
                            } />
                            <Route path="/chat/:odbiorcaId" element={
                                <ProtectedRoute>
                                    <Chat />
                                </ProtectedRoute>
                            } />
                            <Route path="/profil" element={
                                <ProtectedRoute>
                                    <Profile />
                                </ProtectedRoute>
                            } />

                            {/* Protected Routes - Admin */}
                            <Route path="/admin" element={
                                <ProtectedRoute adminOnly>
                                    <AdminPanel />
                                </ProtectedRoute>
                            } />
                            
                            {/* Edit mieszkanie */}
                            <Route path="/edytuj-mieszkanie/:id" element={
                                <ProtectedRoute>
                                    <AddMieszkanie editMode />
                                </ProtectedRoute>
                            } />
                        </Routes>
                    </main>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
