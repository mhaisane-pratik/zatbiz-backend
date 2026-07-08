import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { DashboardTemplateProps } from './types';
import { useCategoryDashboardState } from './useCategoryDashboardState';
import { DashboardDinerOverview } from './DashboardDinerOverview';
import { DashboardBookingsView } from './DashboardBookingsView';
import { DashboardItemsView } from './DashboardItemsView';
import { api, getRandomFoodImage } from '@/services/api';
import { Product } from '@/types';
import { useDarkMode } from '@/hooks/useDarkMode';
import {
  UserProfilePanel,
  UserOrdersPanel,
} from '../../dashboard/UserPanels';
import {
  AdminOverviewPanel,
  AdminProductsPanel,
  AdminSettingsPanel,
} from '../../dashboard/AdminPanels';

export function CategoryDashboardTemplate({
  projectId,
  project,
  clientEmail,
  theme,
  onLogout,
  companyName,
  setCompanyName,
  logoIcon,
  logoUrl,
  shopNiche,
  niche,
  primaryColor,
  accentBg,
  emoji,
  metrics
}: DashboardTemplateProps) {
  const { isDarkMode, toggleDarkMode } = useDarkMode();
  const [activeTab, setActiveTab] = useState<string>('overview');
  const router = useRouter();

  const [cart, setCart] = useState<{ product: Product; quantity: number }[]>([]);

  const handleAddToCart = (p: Product) => {
    setCart((prev) => {
      const existing = prev.find((item) => item.product.id === p.id);
      if (existing) {
        return prev.map((item) =>
          item.product.id === p.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      }
      return [...prev, { product: p, quantity: 1 }];
    });
  };

  const handleUpdateCartQuantity = (productId: number, delta: number) => {
    setCart((prev) => {
      return prev.map((item) => {
        if (item.product.id === productId) {
          const newQty = item.quantity + delta;
          return newQty > 0 ? { ...item, quantity: newQty } : null;
        }
        return item;
      }).filter(Boolean) as { product: Product; quantity: number }[];
    });
  };

  const handleRemoveFromCart = (productId: number) => {
    setCart((prev) => prev.filter((item) => item.product.id !== productId));
  };

  const cartCount = cart.reduce((s, i) => s + i.quantity, 0);

  const {
    products, fetchDbProducts,
    orders, fetchOrders,
    reservations, fetchReservationsList, reservationLoading,
    walletBalance, setWalletBalance,
    storeSettings, setStoreSettings,
    userName, setUserName,
    userPhone, setUserPhone,
    userAddressHome, setUserAddressHome
  } = useCategoryDashboardState(projectId, clientEmail, companyName, logoUrl);

  const [resFormDate, setResFormDate] = useState(() => new Date().toISOString().split('T')[0]);
  const [resFormTime, setResFormTime] = useState('19:00');
  const [resFormGuests, setResFormGuests] = useState(2);
  const [resFormNotes, setResFormNotes] = useState('');

  const handleSaveProfileChanges = async () => {
    const clientId = localStorage.getItem('clientId');
    if (!clientId) return;
    try {
      await api.customers.update(parseInt(clientId, 10), {
        name: userName,
        phone: userPhone,
        address: userAddressHome
      });
      localStorage.setItem('clientName', userName);
      localStorage.setItem('clientPhone', userPhone);
      localStorage.setItem('clientAddress', userAddressHome);
      alert('VIP Profile updated successfully!');
    } catch (err) {
      console.error(err);
      alert('Failed to update profile.');
    }
  };

  const handleCreateBooking = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = {
      projectId,
      customerName: userName || 'Honored Guest',
      customerEmail: clientEmail,
      customerPhone: userPhone,
      bookingDate: resFormDate,
      bookingTime: resFormTime,
      numberOfGuests: resFormGuests,
      tableNumber: 'Auto-Allocated Table',
      notes: resFormNotes,
      status: 'Pending'
    };

    try {
      await api.reservations.create(payload);
      alert('Table reservation requested successfully! Check status below.');
      setResFormNotes('');
      fetchReservationsList();
    } catch (err) {
      console.error(err);
      alert('Failed to submit booking.');
    }
  };

  const handleUpdateReservationStatus = async (id: number, status: string) => {
    try {
      await api.reservations.updateStatus(id, status);
      alert(`Reservation marked as ${status}!`);
      fetchReservationsList();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDeleteBooking = async (id: number) => {
    if (!confirm('Cancel this booking request?')) return;
    try {
      await api.reservations.delete(id);
      fetchReservationsList();
    } catch (err) {
      console.error(err);
    }
  };

  const isDiner = clientEmail !== 'admin@gmail.com';

  useEffect(() => {
    if (!isDiner) {
      setActiveTab('overview');
    } else {
      setActiveTab('dashboard');
    }
  }, [clientEmail]);

  return (
    <div className="flex flex-1 min-h-screen bg-[#faf8f5] text-slate-800 font-sans">
      {/* Category-Specific Sidebar */}
      <aside className="w-64 bg-white border-r border-stone-200/80 p-6 flex flex-col justify-between flex-shrink-0 font-sans">
        <div className="space-y-8 text-left">
          {/* Logo */}
          <div className="flex items-center gap-2">
            <span className="text-3xl select-none">{emoji}</span>
            <div>
              <h3 className="font-extrabold text-slate-800 text-sm uppercase tracking-wider leading-none">
                {companyName.replace(" Site", "")}
              </h3>
              <p className="text-[8px] font-black uppercase tracking-widest mt-1" style={{ color: primaryColor }}>{niche.toUpperCase()} PORTAL</p>
            </div>
          </div>

          {/* Navigation Menu */}
          <nav className="space-y-1">
            {(isDiner
              ? [
                  { id: 'dashboard', label: 'Dashboard', icon: '📊' },
                  { id: 'items', label: 'Plated Menu', icon: emoji },
                  { id: 'bookings', label: 'Reservations', icon: '📅' },
                  { id: 'orders', label: `Order Drafts ${cartCount > 0 ? `(${cartCount})` : ''}`, icon: '🛵' },
                  { id: 'profile', label: 'Profile', icon: '👤' }
                ]
              : [
                  { id: 'overview', label: 'Overview', icon: '📊' },
                  { id: 'bookings', label: 'Table Bookings', icon: '📅' },
                  { id: 'orders', label: 'Diner Orders', icon: '🛵' },
                  { id: 'settings', label: 'Kitchen Settings', icon: '⚙️' }
                ]
            ).map((item) => {
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className="w-full flex items-center gap-3 px-4 py-2.5 text-xs font-bold rounded-xl transition text-left cursor-pointer border border-transparent bg-transparent"
                  style={isActive ? {
                    backgroundColor: `${primaryColor}1a`,
                    color: primaryColor,
                    borderColor: `${primaryColor}33`
                  } : {
                    color: '#94a3b8'
                  }}
                >
                  <span className="text-sm select-none">{item.icon}</span>
                  <span>{item.label}</span>
                </button>
              );
            })}
          </nav>
        </div>

        <div className="space-y-1.5">
          <button
            onClick={toggleDarkMode}
            className="w-full flex items-center gap-3 px-4 py-2.5 text-xs font-bold text-stone-500 hover:text-stone-900 hover:bg-stone-55 rounded-xl transition text-left cursor-pointer border-none bg-transparent uppercase tracking-wider"
          >
            {isDarkMode ? '☀️ Light Mode' : '🌙 Dark Mode'}
          </button>
          <button
            onClick={onLogout}
            className="w-full flex items-center gap-3 px-4 py-2.5 text-xs font-bold text-rose-500 hover:text-rose-455 hover:bg-rose-50 rounded-xl transition text-left cursor-pointer border-none bg-transparent uppercase tracking-wider"
          >
            🚪 Exit Console
          </button>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 bg-[#faf9f6] p-8 overflow-y-auto font-sans">
        <div className="max-w-6xl mx-auto space-y-6">
          
          {/* Header */}
          <header className="flex justify-between items-center pb-6 border-b border-stone-200/50 font-sans">
            <div className="flex items-center gap-3 text-left">
              <div className="w-12 h-12 rounded-full overflow-hidden border bg-stone-50 flex items-center justify-center text-xl" style={{ borderColor: `${primaryColor}40` }}>
                {emoji}
              </div>
              <div className="text-left font-sans">
                <h2 className="text-lg font-black text-slate-800 tracking-tight leading-tight uppercase font-serif">
                  {isDiner ? `Welcome back, ${userName || 'Diner'}` : `${niche} Manager Terminal`}
                </h2>
                <p className="text-[9px] font-bold uppercase tracking-widest mt-0.5" style={{ color: primaryColor }}>
                  {isDiner ? 'FEASTING CATALOG PORTAL' : 'ACTIVE PIPELINE CONTROL'}
                </p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <div className="text-[10px] bg-white border rounded-xl px-4 py-2 font-black shadow-sm flex items-center gap-2 select-none" style={{ borderColor: `${primaryColor}20`, color: primaryColor }}>
                <span className="w-2 h-2 rounded-full animate-pulse" style={{ backgroundColor: primaryColor }} />
                {niche.toUpperCase()} CONSOLE ACTIVE
              </div>
            </div>
          </header>

          {/* DYNAMIC TAB RENDERING */}
          {activeTab === 'dashboard' && isDiner && (
            <DashboardDinerOverview
              primaryColor={primaryColor}
              walletBalance={walletBalance}
              setWalletBalance={setWalletBalance}
              setActiveTab={setActiveTab}
            />
          )}

          {/* Admin overview tab */}
          {activeTab === 'overview' && !isDiner && (
            <div className="space-y-6 text-left">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {metrics.map((stat, i) => (
                  <div key={i} className="bg-white border border-stone-200/80 rounded-3xl p-6">
                    <span className="text-[10px] text-slate-500 font-black uppercase tracking-wider block">{stat.title}</span>
                    <h4 className="text-2xl font-black text-slate-800 mt-1.5 font-mono">{stat.value}</h4>
                    <p className="text-[10px] font-extrabold mt-1" style={{ color: primaryColor }}>✓ {stat.desc}</p>
                  </div>
                ))}
              </div>
              <AdminOverviewPanel
                orders={orders}
                products={products}
                couponsList={[]}
                theme={theme}
                setActiveTab={setActiveTab}
                isRestaurant={true}
              />
            </div>
          )}

          {/* Products Panel (Admin) */}
          {activeTab === 'overview' && !isDiner && (
            <div className="pt-6">
              <AdminProductsPanel
                projectId={projectId}
                products={products}
                categoriesList={[]}
                brandsList={[]}
                fetchDbProducts={fetchDbProducts}
                isRestaurant={true}
                shopNiche={shopNiche}
              />
            </div>
          )}

          {/* Bookings/Reservations Tab */}
          {activeTab === 'bookings' && (
            <DashboardBookingsView
              isDiner={isDiner}
              primaryColor={primaryColor}
              reservationLoading={reservationLoading}
              reservations={reservations}
              handleCreateBooking={handleCreateBooking}
              handleUpdateReservationStatus={handleUpdateReservationStatus}
              handleDeleteBooking={handleDeleteBooking}
              resFormDate={resFormDate}
              setResFormDate={setResFormDate}
              resFormTime={resFormTime}
              setResFormTime={setResFormTime}
              resFormGuests={resFormGuests}
              setResFormGuests={setResFormGuests}
              resFormNotes={resFormNotes}
              setResFormNotes={setResFormNotes}
            />
          )}

          {/* Plated Menu Tab (Diner) */}
          {activeTab === 'items' && isDiner && (
            <DashboardItemsView
              products={products}
              primaryColor={primaryColor}
              onAddToCart={handleAddToCart}
            />
          )}

          {/* Orders Log (Diner) */}
          {activeTab === 'orders' && isDiner && (
            <div className="space-y-6">
              {/* Bucket / Draft Order Checkout */}
              {cart.length > 0 && (
                <div className="bg-white border rounded-3xl p-6 text-left font-sans animate-fadeIn" style={{ borderColor: `${primaryColor}33` }}>
                  <div className="flex justify-between items-center pb-3 border-b border-stone-100 mb-4">
                    <h3 className="text-xs font-black text-slate-800 uppercase tracking-widest font-serif">
                      🛒 Current Order Draft (Bucket)
                    </h3>
                    <span className="text-[10px] bg-slate-100 text-slate-500 px-2 py-0.5 rounded-full font-bold">
                      {cartCount} items in bucket
                    </span>
                  </div>

                  <div className="divide-y divide-stone-100 max-h-80 overflow-y-auto mb-6 pr-2">
                    {cart.map((item) => {
                      const itemSubtotal = item.product.price * item.quantity;
                      return (
                        <div key={item.product.id} className="py-4 first:pt-0 last:pb-0 flex justify-between items-center">
                          <div className="flex items-center gap-3">
                            <div className="w-12 h-12 rounded-xl bg-stone-50 border overflow-hidden flex-shrink-0">
                              <img src={item.product.imageUrl || getRandomFoodImage(item.product.name)} className="w-full h-full object-cover" alt={item.product.name} />
                            </div>
                            <div>
                              <h4 className="text-xs font-black text-slate-800 uppercase truncate max-w-[200px]">{item.product.name}</h4>
                              <p className="text-[10px] font-bold text-slate-400 mt-0.5">₹{item.product.price} each</p>
                            </div>
                          </div>

                          <div className="flex items-center gap-6">
                            {/* Quantity Adjusters */}
                            <div className="flex items-center border border-stone-200 rounded-xl overflow-hidden bg-stone-50">
                              <button
                                onClick={() => handleUpdateCartQuantity(item.product.id!, -1)}
                                className="px-2.5 py-1 text-slate-500 hover:bg-stone-100 font-black cursor-pointer border-none bg-transparent"
                              >
                                -
                              </button>
                              <span className="px-2 text-xs font-black text-slate-800 select-none">
                                {item.quantity}
                              </span>
                              <button
                                onClick={() => handleUpdateCartQuantity(item.product.id!, 1)}
                                className="px-2.5 py-1 text-slate-500 hover:bg-stone-100 font-black cursor-pointer border-none bg-transparent"
                              >
                                +
                              </button>
                            </div>

                            <div className="text-right w-24">
                              <span className="text-xs font-black text-slate-800 block">
                                ₹{itemSubtotal.toFixed(2)}
                              </span>
                              <button
                                onClick={() => handleRemoveFromCart(item.product.id!)}
                                className="text-[9px] text-rose-500 hover:underline font-bold bg-transparent border-none cursor-pointer p-0"
                              >
                                Remove
                              </button>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>

                  {/* Summary & Place Order */}
                  {(() => {
                    const subtotal = cart.reduce((s, i) => s + i.product.price * i.quantity, 0);
                    const tax = subtotal * 0.05; // 5% GST
                    const total = subtotal + tax;

                    const handlePlaceDinerOrder = async (method: 'COD' | 'Wallet') => {
                      const clientId = localStorage.getItem('clientId');
                      if (method === 'Wallet' && walletBalance < total) {
                        alert(`❌ Insufficient wallet balance! You need ₹${total.toFixed(2)} but have ₹${walletBalance.toFixed(2)}.`);
                        return;
                      }

                      const orderPayload = {
                        projectId,
                        customerId: clientId ? parseInt(clientId, 10) : null,
                        customerName: userName || 'Diner Guest',
                        customerEmail: clientEmail,
                        customerPhone: userPhone || '',
                        customerAddress: userAddressHome || 'Dine-In Table / Room Service',
                        itemsJson: JSON.stringify(
                          cart.map((item) => ({
                            id: item.product.id,
                            name: item.product.name,
                            price: item.product.price,
                            quantity: item.quantity,
                            imageUrl: item.product.imageUrl
                          }))
                        ),
                        subtotal: parseFloat(subtotal.toFixed(2)),
                        tax: parseFloat(tax.toFixed(2)),
                        total: parseFloat(total.toFixed(2)),
                        paymentGateway: method === 'Wallet' ? 'Wallet Pay' : 'Stripe',
                        paymentStatus: method === 'Wallet' ? 'Paid' : 'Pending COD Verification',
                        status: 'Preparing', // Immediate kitchen status
                        city: 'Noida',
                        state: 'UP',
                        pincode: '201301',
                        paymentMethod: method === 'Wallet' ? 'Wallet' : 'COD'
                      };

                      try {
                        await api.orders.place(orderPayload);
                        if (method === 'Wallet') {
                          setWalletBalance((prev) => parseFloat((prev - total).toFixed(2)));
                        }
                        alert(`🎉 Feast order sent to kitchen successfully! Status: PREPARING via ${method}.`);
                        setCart([]);
                        fetchOrders();
                      } catch (err) {
                        console.error('Failed to place order:', err);
                        alert('Failed to place order. Please try again.');
                      }
                    };

                    return (
                      <div className="border-t border-stone-100 pt-4 space-y-4">
                        <div className="flex flex-col items-end gap-1.5 text-xs text-slate-500 font-bold">
                          <div className="flex justify-between w-64">
                            <span>Subtotal:</span>
                            <span className="text-slate-800">₹{subtotal.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between w-64">
                            <span>GST (5%):</span>
                            <span className="text-slate-800">₹{tax.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between w-64 border-t border-stone-100 pt-1.5 text-sm font-black text-slate-800">
                            <span>Grand Total:</span>
                            <span style={{ color: primaryColor }}>₹{total.toFixed(2)}</span>
                          </div>
                        </div>

                        <div className="flex justify-end gap-3 pt-2">
                          <button
                            onClick={() => handlePlaceDinerOrder('COD')}
                            className="px-6 py-2.5 bg-white border border-stone-200 hover:bg-stone-50 text-slate-700 font-black rounded-xl text-[10px] uppercase tracking-wider transition shadow-sm cursor-pointer"
                          >
                            Send Order (Cash on Delivery)
                          </button>
                          <button
                            onClick={() => handlePlaceDinerOrder('Wallet')}
                            className="px-6 py-2.5 text-slate-850 font-black rounded-xl text-[10px] uppercase tracking-wider transition border-none shadow-sm cursor-pointer"
                            style={{ backgroundColor: primaryColor }}
                          >
                            Pay with VIP Wallet (Bal: ₹{walletBalance.toFixed(2)})
                          </button>
                        </div>
                      </div>
                    );
                  })()}
                </div>
              )}

              {/* History list */}
              <div className="bg-white border rounded-3xl p-6 text-left font-sans" style={{ borderColor: `${primaryColor}20` }}>
                <h3 className="text-xs font-black text-slate-800 uppercase tracking-widest mb-6 font-serif">Diner Order History</h3>
                <UserOrdersPanel orders={orders} clientEmail={clientEmail} shopNiche={shopNiche} theme={theme} />
              </div>
            </div>
          )}

          {/* Settings Console (Admin) */}
          {activeTab === 'settings' && !isDiner && (
            <div className="font-sans">
              <AdminSettingsPanel
                projectId={projectId}
                storeSettings={storeSettings}
                setStoreSettings={setStoreSettings}
                setCompanyName={setCompanyName}
              />
            </div>
          )}

          {/* Profile Tab (Diner) */}
          {activeTab === 'profile' && isDiner && (
            <div className="text-left font-sans animate-fadeIn">
              <UserProfilePanel
                userName={userName}
                setUserName={setUserName}
                userPhone={userPhone}
                setUserPhone={setUserPhone}
                userAddressHome={userAddressHome}
                setUserAddressHome={setUserAddressHome}
                clientEmail={clientEmail}
                theme={theme}
                shopNiche={shopNiche}
                handleSaveProfileChanges={handleSaveProfileChanges}
              />
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
