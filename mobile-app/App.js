import React, { useState } from 'react';
import {
  SafeAreaView,
  ScrollView,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Linking,
  Alert,
  StatusBar,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';

const services = [
  ['shirt-outline', 'Shirt Stitching', 'Perfect fitting with clean finishing'],
  ['body-outline', 'Pant Stitching', 'Classic and modern trouser fitting'],
  ['sparkles-outline', 'Suit & Blazer', 'Premium tailoring for special occasions'],
  ['cut-outline', 'Alteration', 'Fast and accurate fitting corrections'],
];

export default function App() {
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [service, setService] = useState('');

  const openWhatsApp = () => {
    const text = encodeURIComponent('Assalamualaikum, mujhe tailoring service ke baare me jankari chahiye.');
    Linking.openURL(`https://wa.me/?text=${text}`);
  };

  const callShop = () => Linking.openURL('tel:+917565053878');

  const submitBooking = () => {
    if (!name.trim() || !phone.trim() || !service.trim()) {
      Alert.alert('Details required', 'Please fill name, phone and service.');
      return;
    }
    Alert.alert('Request sent', 'Your booking request has been prepared successfully.');
  };

  return (
    <SafeAreaView style={styles.safe}>
      <StatusBar barStyle="light-content" backgroundColor="#111827" />
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.hero}>
          <View style={styles.brandRow}>
            <View style={styles.logoCircle}><Text style={styles.logoText}>NS</Text></View>
            <View>
              <Text style={styles.brand}>NEW STAR</Text>
              <Text style={styles.subBrand}>SHADAB TAILOR</Text>
            </View>
          </View>
          <Text style={styles.heroTitle}>Premium Tailoring. Perfect Fit.</Text>
          <Text style={styles.heroText}>Professional stitching and alteration services crafted with attention to every detail.</Text>
          <View style={styles.heroButtons}>
            <TouchableOpacity style={styles.primaryBtn} onPress={openWhatsApp}>
              <Ionicons name="logo-whatsapp" size={20} color="#fff" />
              <Text style={styles.primaryBtnText}>WhatsApp</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.secondaryBtn} onPress={callShop}>
              <Ionicons name="call-outline" size={20} color="#111827" />
              <Text style={styles.secondaryBtnText}>Call Now</Text>
            </TouchableOpacity>
          </View>
        </View>

        <Text style={styles.sectionTitle}>Our Services</Text>
        <View style={styles.grid}>
          {services.map(([icon, title, desc]) => (
            <View style={styles.card} key={title}>
              <View style={styles.iconWrap}><Ionicons name={icon} size={25} color="#111827" /></View>
              <Text style={styles.cardTitle}>{title}</Text>
              <Text style={styles.cardText}>{desc}</Text>
            </View>
          ))}
        </View>

        <View style={styles.highlight}>
          <Ionicons name="shield-checkmark-outline" size={30} color="#fff" />
          <View style={{flex:1}}>
            <Text style={styles.highlightTitle}>Why choose us?</Text>
            <Text style={styles.highlightText}>Clean finishing • Accurate fitting • Reliable service • Premium craftsmanship</Text>
          </View>
        </View>

        <Text style={styles.sectionTitle}>Book a Stitching Request</Text>
        <View style={styles.formCard}>
          <TextInput placeholder="Your name" value={name} onChangeText={setName} style={styles.input} placeholderTextColor="#9CA3AF" />
          <TextInput placeholder="Phone number" keyboardType="phone-pad" value={phone} onChangeText={setPhone} style={styles.input} placeholderTextColor="#9CA3AF" />
          <TextInput placeholder="Service required (e.g. Shirt, Pant, Suit)" value={service} onChangeText={setService} style={styles.input} placeholderTextColor="#9CA3AF" />
          <TouchableOpacity style={styles.bookBtn} onPress={submitBooking}>
            <Text style={styles.bookBtnText}>Submit Request</Text>
            <Ionicons name="arrow-forward" size={20} color="#fff" />
          </TouchableOpacity>
        </View>

        <View style={styles.footer}>
          <Text style={styles.footerBrand}>New Star Shadab Tailor</Text>
          <Text style={styles.footerText}>Jaunpur, Uttar Pradesh</Text>
          <Text style={styles.footerText}>Made for better customer service</Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe:{flex:1,backgroundColor:'#F4F5F7'},
  container:{paddingBottom:30},
  hero:{backgroundColor:'#111827',padding:24,paddingTop:30,borderBottomLeftRadius:30,borderBottomRightRadius:30},
  brandRow:{flexDirection:'row',alignItems:'center',gap:12,marginBottom:32},
  logoCircle:{width:48,height:48,borderRadius:24,backgroundColor:'#F59E0B',alignItems:'center',justifyContent:'center'},
  logoText:{fontWeight:'900',fontSize:17,color:'#111827'},
  brand:{fontWeight:'900',fontSize:16,color:'#fff',letterSpacing:1.6},
  subBrand:{fontSize:11,color:'#F59E0B',fontWeight:'700',letterSpacing:2},
  heroTitle:{fontSize:32,lineHeight:38,fontWeight:'900',color:'#fff',maxWidth:320},
  heroText:{color:'#D1D5DB',fontSize:15,lineHeight:22,marginTop:12},
  heroButtons:{flexDirection:'row',gap:10,marginTop:24},
  primaryBtn:{flex:1,flexDirection:'row',gap:8,backgroundColor:'#16A34A',paddingVertical:14,borderRadius:14,alignItems:'center',justifyContent:'center'},
  primaryBtnText:{color:'#fff',fontWeight:'800'},
  secondaryBtn:{flex:1,flexDirection:'row',gap:8,backgroundColor:'#fff',paddingVertical:14,borderRadius:14,alignItems:'center',justifyContent:'center'},
  secondaryBtnText:{color:'#111827',fontWeight:'800'},
  sectionTitle:{fontSize:22,fontWeight:'900',color:'#111827',marginTop:26,marginBottom:14,paddingHorizontal:20},
  grid:{flexDirection:'row',flexWrap:'wrap',paddingHorizontal:14},
  card:{width:'50%',padding:6},
  iconWrap:{width:45,height:45,borderRadius:14,backgroundColor:'#FEF3C7',alignItems:'center',justifyContent:'center',marginBottom:12},
  cardTitle:{fontSize:16,fontWeight:'800',color:'#111827'},
  cardText:{fontSize:12.5,lineHeight:18,color:'#6B7280',marginTop:5},
  highlight:{margin:20,marginTop:26,borderRadius:20,backgroundColor:'#1F2937',padding:20,flexDirection:'row',gap:14,alignItems:'center'},
  highlightTitle:{color:'#fff',fontSize:17,fontWeight:'900'},
  highlightText:{color:'#D1D5DB',lineHeight:20,marginTop:4,fontSize:13},
  formCard:{marginHorizontal:20,backgroundColor:'#fff',borderRadius:20,padding:16,elevation:2},
  input:{borderWidth:1,borderColor:'#E5E7EB',borderRadius:12,paddingHorizontal:14,paddingVertical:13,marginBottom:11,color:'#111827'},
  bookBtn:{backgroundColor:'#F59E0B',paddingVertical:15,borderRadius:13,flexDirection:'row',alignItems:'center',justifyContent:'center',gap:8},
  bookBtnText:{color:'#111827',fontWeight:'900',fontSize:15},
  footer:{alignItems:'center',paddingTop:30,paddingHorizontal:20},
  footerBrand:{fontWeight:'900',color:'#111827'},
  footerText:{fontSize:12,color:'#9CA3AF',marginTop:4},
});
