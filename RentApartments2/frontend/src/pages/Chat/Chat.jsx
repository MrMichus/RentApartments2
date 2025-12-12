import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { chatService } from '../../api/services';
import { useAuth } from '../../context/AuthContext';
import './Chat.css';

const Chat = () => {
    const { odbiorcaId } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    
    const [conversations, setConversations] = useState([]);
    const [selectedChat, setSelectedChat] = useState(null);
    const [currentChatData, setCurrentChatData] = useState(null);
    const [newMessage, setNewMessage] = useState('');
    const [loading, setLoading] = useState(true);
    const [sendingMessage, setSendingMessage] = useState(false);
    const [error, setError] = useState('');
    
    const messagesEndRef = useRef(null);

    useEffect(() => {
        fetchConversations();
    }, []);

    // Jeśli przekazano odbiorcaId w URL, otwórz lub utwórz czat z tym użytkownikiem
    useEffect(() => {
        if (odbiorcaId && !loading) {
            openChatWithUser(odbiorcaId);
        }
    }, [odbiorcaId, loading]);

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [currentChatData?.wiadomosci]);

    const fetchConversations = async () => {
        try {
            setLoading(true);
            const response = await chatService.getMyChats();
            setConversations(response.data);
        } catch (err) {
            setError('Błąd podczas ładowania konwersacji');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const openChatWithUser = async (userId) => {
        try {
            const response = await chatService.getOrCreateChat(userId);
            setCurrentChatData(response.data);
            setSelectedChat(response.data.id);
            
            // Aktualizuj URL bez przeładowania
            navigate(`/chat`, { replace: true });
            
            // Odśwież listę konwersacji
            fetchConversations();
        } catch (err) {
            setError('Nie udało się otworzyć konwersacji');
            console.error(err);
        }
    };

    const selectConversation = async (chatId) => {
        try {
            const response = await chatService.getChatById(chatId);
            setCurrentChatData(response.data);
            setSelectedChat(chatId);
        } catch (err) {
            setError('Błąd podczas ładowania wiadomości');
        }
    };

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!newMessage.trim() || !currentChatData) return;

        setSendingMessage(true);
        setError('');
        
        try {
            // Wyślij wiadomość do odbiorcy
            const response = await chatService.sendMessage(
                currentChatData.recipientId, 
                newMessage
            );
            setNewMessage('');
            setCurrentChatData(response.data);
            
            // Odśwież listę konwersacji
            fetchConversations();
        } catch (err) {
            setError('Błąd podczas wysyłania wiadomości');
            console.error(err);
        } finally {
            setSendingMessage(false);
        }
    };

    const formatTime = (dateString) => {
        return new Date(dateString).toLocaleTimeString('pl-PL', {
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const today = new Date();
        const yesterday = new Date(today);
        yesterday.setDate(yesterday.getDate() - 1);
        
        if (date.toDateString() === today.toDateString()) {
            return 'Dzisiaj';
        } else if (date.toDateString() === yesterday.toDateString()) {
            return 'Wczoraj';
        }
        return date.toLocaleDateString('pl-PL', {
            day: 'numeric',
            month: 'short'
        });
    };

    const getOtherUserName = (chat) => {
        // Jeśli aktualny użytkownik jest senderem, pokaż recipientName
        if (chat.senderId === user?.id) {
            return chat.recipientName;
        }
        return chat.senderName;
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Ładowanie czatu...</p>
            </div>
        );
    }

    return (
        <div className="chat-page">
            <div className="chat-container">
                {/* Sidebar - Lista konwersacji */}
                <div className="chat-sidebar">
                    <div className="sidebar-header">
                        <h2>💬 Wiadomości</h2>
                    </div>

                    <div className="conversations-list">
                        {conversations.length === 0 ? (
                            <div className="no-conversations">
                                <p>Brak konwersacji</p>
                                <span>Napisz do właściciela mieszkania!</span>
                            </div>
                        ) : (
                            conversations.map(conv => (
                                <div
                                    key={conv.id}
                                    className={`conversation-item ${selectedChat === conv.id ? 'active' : ''}`}
                                    onClick={() => selectConversation(conv.id)}
                                >
                                    <div className="conv-avatar">👤</div>
                                    <div className="conv-info">
                                        <span className="conv-name">
                                            {getOtherUserName(conv)}
                                        </span>
                                        <span className="conv-preview">
                                            {conv.wiadomosci && conv.wiadomosci.length > 0 
                                                ? conv.wiadomosci[conv.wiadomosci.length - 1].tresc.substring(0, 30) + '...'
                                                : 'Brak wiadomości'
                                            }
                                        </span>
                                    </div>
                                    {conv.updatedAt && (
                                        <span className="conv-time">{formatDate(conv.updatedAt)}</span>
                                    )}
                                </div>
                            ))
                        )}
                    </div>
                </div>

                {/* Main Chat Area */}
                <div className="chat-main">
                    {error && <div className="error-message">{error}</div>}
                    
                    {currentChatData ? (
                        <>
                            <div className="chat-header">
                                <div className="chat-header-avatar">👤</div>
                                <div className="chat-header-info">
                                    <h3>{getOtherUserName(currentChatData)}</h3>
                                </div>
                            </div>

                            <div className="messages-container">
                                {!currentChatData.wiadomosci || currentChatData.wiadomosci.length === 0 ? (
                                    <div className="no-messages">
                                        <span>💬</span>
                                        <p>Brak wiadomości</p>
                                        <span>Napisz pierwszą wiadomość!</span>
                                    </div>
                                ) : (
                                    currentChatData.wiadomosci.map((msg, index) => (
                                        <div
                                            key={index}
                                            className={`message ${msg.userId === user?.id ? 'sent' : 'received'}`}
                                        >
                                            <div className="message-content">
                                                <p>{msg.tresc}</p>
                                                <span className="message-time">
                                                    {formatTime(msg.timestamp)}
                                                </span>
                                            </div>
                                        </div>
                                    ))
                                )}
                                <div ref={messagesEndRef} />
                            </div>

                            <form onSubmit={handleSendMessage} className="message-form">
                                <input
                                    type="text"
                                    placeholder="Napisz wiadomość..."
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    disabled={sendingMessage}
                                />
                                <button type="submit" disabled={sendingMessage || !newMessage.trim()}>
                                    {sendingMessage ? '...' : '➤'}
                                </button>
                            </form>
                        </>
                    ) : (
                        <div className="no-chat-selected">
                            <span>💬</span>
                            <h3>Wybierz konwersację</h3>
                            <p>Wybierz rozmowę z listy lub przejdź do mieszkania i napisz do właściciela</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Chat;
