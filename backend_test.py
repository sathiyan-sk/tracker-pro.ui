#!/usr/bin/env python3
"""
Comprehensive Backend API Testing for Tracker Pro Spring Boot Application
Tests all authentication endpoints and functionality
"""

import requests
import json
import sys
from datetime import datetime

class TrackerProAPITester:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.tests_run = 0
        self.tests_passed = 0
        self.test_results = []

    def log_test(self, name, success, message="", response_data=None):
        """Log test results"""
        self.tests_run += 1
        if success:
            self.tests_passed += 1
            status = "✅ PASS"
        else:
            status = "❌ FAIL"
        
        result = {
            'name': name,
            'success': success,
            'message': message,
            'response_data': response_data
        }
        self.test_results.append(result)
        print(f"{status} - {name}: {message}")
        return success

    def test_server_health(self):
        """Test if server is running"""
        try:
            response = self.session.get(f"{self.base_url}/")
            success = response.status_code == 200
            return self.log_test(
                "Server Health Check",
                success,
                f"Status: {response.status_code}" if success else f"Server not responding: {response.status_code}"
            )
        except Exception as e:
            return self.log_test("Server Health Check", False, f"Connection error: {str(e)}")

    def test_admin_login(self):
        """Test admin login with default credentials"""
        login_data = {
            "email": "admin@trackerpro.com",
            "password": "admin123"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/login",
                json=login_data,
                headers={'Content-Type': 'application/json'}
            )
            
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('success', False)
                message = f"Login successful, Role: {data.get('user', {}).get('role', 'Unknown')}"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            return self.log_test("Admin Login", success, message, response.json() if success else None)
            
        except Exception as e:
            return self.log_test("Admin Login", False, f"Error: {str(e)}")

    def test_invalid_login(self):
        """Test login with invalid credentials"""
        login_data = {
            "email": "invalid@test.com",
            "password": "wrongpassword"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/login",
                json=login_data,
                headers={'Content-Type': 'application/json'}
            )
            
            # Should return 400 for invalid credentials
            success = response.status_code == 400
            if success:
                data = response.json()
                success = not data.get('success', True)  # success should be false
                message = f"Correctly rejected invalid login: {data.get('message', '')}"
            else:
                message = f"Unexpected status code: {response.status_code}"
                
            return self.log_test("Invalid Login Rejection", success, message)
            
        except Exception as e:
            return self.log_test("Invalid Login Rejection", False, f"Error: {str(e)}")

    def test_user_registration(self):
        """Test user registration"""
        timestamp = datetime.now().strftime("%H%M%S")
        registration_data = {
            "fullName": f"Test User {timestamp}",
            "email": f"testuser{timestamp}@example.com",
            "password": "testpass123",
            "confirmPassword": "testpass123",
            "department": "IT",
            "empId": f"EMP{timestamp}",
            "mobileNo": "+91-9876543210"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/register",
                json=registration_data,
                headers={'Content-Type': 'application/json'}
            )
            
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('success', False)
                message = f"Registration successful, User ID: {data.get('userId', 'Unknown')}"
                # Store for later login test
                self.test_user_email = registration_data['email']
                self.test_user_password = registration_data['password']
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            return self.log_test("User Registration", success, message, response.json() if success else None)
            
        except Exception as e:
            return self.log_test("User Registration", False, f"Error: {str(e)}")

    def test_registered_user_login(self):
        """Test login with newly registered user"""
        if not hasattr(self, 'test_user_email'):
            return self.log_test("Registered User Login", False, "No test user available (registration failed)")
            
        login_data = {
            "email": self.test_user_email,
            "password": self.test_user_password
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/login",
                json=login_data,
                headers={'Content-Type': 'application/json'}
            )
            
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('success', False)
                message = f"Login successful, Role: {data.get('user', {}).get('role', 'Unknown')}"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            return self.log_test("Registered User Login", success, message)
            
        except Exception as e:
            return self.log_test("Registered User Login", False, f"Error: {str(e)}")

    def test_duplicate_email_registration(self):
        """Test registration with duplicate email"""
        registration_data = {
            "fullName": "Duplicate User",
            "email": "admin@trackerpro.com",  # Use existing admin email
            "password": "testpass123",
            "confirmPassword": "testpass123",
            "department": "IT",
            "empId": "EMPDUP001",
            "mobileNo": "+91-9876543210"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/register",
                json=registration_data,
                headers={'Content-Type': 'application/json'}
            )
            
            # Should return 400 for duplicate email
            success = response.status_code == 400
            if success:
                data = response.json()
                success = not data.get('success', True)  # success should be false
                message = f"Correctly rejected duplicate email: {data.get('message', '')}"
            else:
                message = f"Unexpected status code: {response.status_code}"
                
            return self.log_test("Duplicate Email Rejection", success, message)
            
        except Exception as e:
            return self.log_test("Duplicate Email Rejection", False, f"Error: {str(e)}")

    def test_password_mismatch_registration(self):
        """Test registration with password mismatch"""
        timestamp = datetime.now().strftime("%H%M%S")
        registration_data = {
            "fullName": f"Mismatch User {timestamp}",
            "email": f"mismatch{timestamp}@example.com",
            "password": "testpass123",
            "confirmPassword": "differentpass123",  # Different password
            "department": "IT",
            "empId": f"EMPMIS{timestamp}",
            "mobileNo": "+91-9876543210"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/auth/register",
                json=registration_data,
                headers={'Content-Type': 'application/json'}
            )
            
            # Should return 400 for password mismatch
            success = response.status_code == 400
            if success:
                data = response.json()
                success = not data.get('success', True)  # success should be false
                message = f"Correctly rejected password mismatch: {data.get('message', '')}"
            else:
                message = f"Unexpected status code: {response.status_code}"
                
            return self.log_test("Password Mismatch Rejection", success, message)
            
        except Exception as e:
            return self.log_test("Password Mismatch Rejection", False, f"Error: {str(e)}")

    def test_check_email_endpoint(self):
        """Test email existence check endpoint"""
        try:
            # Test with existing email
            response = self.session.get(f"{self.base_url}/api/auth/check-email?email=admin@trackerpro.com")
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('exists', False) == True
                message = "Correctly identified existing email"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            result1 = self.log_test("Check Email - Existing", success, message)
            
            # Test with non-existing email
            response = self.session.get(f"{self.base_url}/api/auth/check-email?email=nonexistent@test.com")
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('exists', True) == False
                message = "Correctly identified non-existing email"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            result2 = self.log_test("Check Email - Non-existing", success, message)
            
            return result1 and result2
            
        except Exception as e:
            self.log_test("Check Email - Existing", False, f"Error: {str(e)}")
            self.log_test("Check Email - Non-existing", False, f"Error: {str(e)}")
            return False

    def test_check_empid_endpoint(self):
        """Test employee ID existence check endpoint"""
        try:
            # Test with existing empId (admin's empId should be ADMIN001 or similar)
            response = self.session.get(f"{self.base_url}/api/auth/check-empid?empId=ADMIN001")
            success = response.status_code == 200
            if success:
                data = response.json()
                # This might not exist, so we just check the endpoint works
                message = f"Endpoint working, exists: {data.get('exists', False)}"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            result1 = self.log_test("Check EmpId - Test", success, message)
            
            # Test with definitely non-existing empId
            response = self.session.get(f"{self.base_url}/api/auth/check-empid?empId=NONEXISTENT999")
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('exists', True) == False
                message = "Correctly identified non-existing empId"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            result2 = self.log_test("Check EmpId - Non-existing", success, message)
            
            return result1 and result2
            
        except Exception as e:
            self.log_test("Check EmpId - Test", False, f"Error: {str(e)}")
            self.log_test("Check EmpId - Non-existing", False, f"Error: {str(e)}")
            return False

    def test_logout_endpoint(self):
        """Test logout endpoint"""
        try:
            response = self.session.post(f"{self.base_url}/api/auth/logout")
            success = response.status_code == 200
            if success:
                data = response.json()
                success = data.get('success', False)
                message = f"Logout successful: {data.get('message', '')}"
            else:
                message = f"HTTP {response.status_code}: {response.text}"
                
            return self.log_test("Logout", success, message)
            
        except Exception as e:
            return self.log_test("Logout", False, f"Error: {str(e)}")

    def run_all_tests(self):
        """Run all API tests"""
        print("🚀 Starting Tracker Pro API Tests")
        print("=" * 50)
        
        # Basic connectivity
        self.test_server_health()
        
        # Authentication tests
        self.test_admin_login()
        self.test_invalid_login()
        
        # Registration tests
        self.test_user_registration()
        self.test_registered_user_login()
        self.test_duplicate_email_registration()
        self.test_password_mismatch_registration()
        
        # Utility endpoint tests
        self.test_check_email_endpoint()
        self.test_check_empid_endpoint()
        
        # Session management
        self.test_logout_endpoint()
        
        # Print summary
        print("\n" + "=" * 50)
        print(f"📊 Test Summary: {self.tests_passed}/{self.tests_run} tests passed")
        
        if self.tests_passed == self.tests_run:
            print("🎉 All tests passed!")
            return 0
        else:
            print("❌ Some tests failed!")
            print("\nFailed Tests:")
            for result in self.test_results:
                if not result['success']:
                    print(f"  - {result['name']}: {result['message']}")
            return 1

def main():
    """Main test execution"""
    tester = TrackerProAPITester()
    return tester.run_all_tests()

if __name__ == "__main__":
    sys.exit(main())