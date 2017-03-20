	.text
	.globl main
f:
	subq $32, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, 8(%rsp)
	movq %rdx, 16(%rsp)
	movq %rcx, 24(%rsp)
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L2
	movq 0(%rsp), %rdi
	call putchar
	movq 8(%rsp), %rdi
	movq 16(%rsp), %rsi
	movq 24(%rsp), %rdx
	movq 0(%rsp), %rcx
	call f
L1:
	addq $32, %rsp
	ret
L2:
	movq $0, %rax
	jmp L1
main:
	movq $65, %rdi
	movq $66, %rsi
	movq $67, %rdx
	movq $0, %rcx
	call f
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
