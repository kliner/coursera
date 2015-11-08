function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

Y = zeros(size(y,1), num_labels);
for i = 1 : size(y)
  Y(i, y(i)) = 1;
end
a = X;
X = [ones(size(X, 1), 1) X];
X1 = X * Theta1';
X1 = sigmoid(X1);
X1 = [ones(size(X1, 1), 1) X1];
X2 = X1 * Theta2';
X2 = sigmoid(X2);

Y = Y';
Y2 = 1 - Y;
for i = 1 : num_labels
  J = J + (- Y(i, :) * log ( X2(:, i) ) - Y2(i, :) * log ( 1 - X2(:, i) ) ) / m;
end

t = 0;
for i = 1 : input_layer_size
  for j = 1 : hidden_layer_size
    t = t + Theta1(j, i + 1)^2;
  end
end

for i = 1 : hidden_layer_size
  for j = 1 : num_labels
    t = t + Theta2(j, i + 1)^2;
  end
end

J = J + lambda * t / 2 / m;

% -------------------------------------------------------------

for t = 1 : m
  a1 = X(t, :);
  z2 = a1 * Theta1';
%size(z2)
  a2 = sigmoid(z2);
  a2 = [1, a2];
  z3 = a2 * Theta2';
  a3 = sigmoid(z3);
%size(a3)
  d3 = a3 - Y(:, t)';

  d2 = Theta2' * d3';
  d2(1, :) = [];
%size(d2)
  d2 = d2 .* sigmoidGradient(z2)';
%size(d2)
%size(Theta1_grad)
  Theta1_grad = Theta1_grad + d2 * a1;
  Theta2_grad = Theta2_grad + d3' * a2;
end
regular1 = lambda * Theta1;
regular1(:, 1) = 0;
%size(regular1)
%size(Theta1_grad)
regular2 = lambda * Theta2;
regular2(:, 1) = 0;
Theta1_grad = Theta1_grad + regular1;
Theta1_grad = Theta1_grad / m;
Theta2_grad = Theta2_grad + regular2;
Theta2_grad = Theta2_grad / m;

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
